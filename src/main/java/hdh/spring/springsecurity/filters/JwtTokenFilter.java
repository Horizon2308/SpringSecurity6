package hdh.spring.springsecurity.filters;

import hdh.spring.springsecurity.components.JwtTokenUtils;
import hdh.spring.springsecurity.models.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized"
            );
            return;
        }
        final String token = authHeader.substring(7);
        final String username = jwtTokenUtils.exactUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final User userDetails = (User) userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtils.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
            Pair.of(String.format("%s/user/register", apiPrefix), "POST"),
            Pair.of(String.format("%s/user/login", apiPrefix), "POST"),
            Pair.of(String.format("%s/not-authentication**", apiPrefix), "GET"),
            Pair.of(String.format("%s/admin/role**", apiPrefix), "POST")
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        for (var token : bypassTokens) {
            String path = token.getFirst();
            String method = token.getSecond();
            if (requestPath.matches(path.replace("**", "*."))
                    && requestMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

}
