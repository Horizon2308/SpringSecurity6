package hdh.spring.springsecurity.services;

import hdh.spring.springsecurity.components.JwtTokenUtils;
import hdh.spring.springsecurity.exceptions.DataNotFoundException;
import hdh.spring.springsecurity.exceptions.InvalidParamException;
import hdh.spring.springsecurity.exceptions.UsernameOrPasswordIsInvalid;
import hdh.spring.springsecurity.models.dtos.UserDTO;
import hdh.spring.springsecurity.models.dtos.UserLoginDTO;
import hdh.spring.springsecurity.models.entities.User;
import hdh.spring.springsecurity.repositories.RoleRepository;
import hdh.spring.springsecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean existedUser(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public User getUserByUsername(String username) throws DataNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public String createUser(UserDTO userDTO) throws DataNotFoundException, InvalidParamException {
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new InvalidParamException("Password and Retype password don't match");
        }
        User user = userRepository.save(User.builder()
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .role(roleRepository.findById(userDTO.getRoleId())
                        .orElseThrow(() -> new DataNotFoundException("Role not found!")))
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build());
        return jwtTokenUtils.generateToken(user);
    }

    @Override
    public String validateUser(UserLoginDTO userLoginDTO) throws InvalidParamException, UsernameOrPasswordIsInvalid {
        var isAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDTO.getUsername(),
                userLoginDTO.getPassword()
        ));
        if (isAuthentication.isAuthenticated()) {
            User user = (User) isAuthentication.getPrincipal();
            assert user != null;
            return jwtTokenUtils.generateToken(user);
        } else {
            throw new UsernameOrPasswordIsInvalid("Username or password is wrong!");
        }
    }

}
