package hdh.spring.springsecurity.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/test")
public class TestController {

    @GetMapping("not-authentication")
    public ResponseEntity<String> notAuthentication() {
        return ResponseEntity.ok("This method doesn't need authentication");
    }

    @GetMapping("authentication-with-role-admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<String> authenticationWithRoleAdmin() {
        return ResponseEntity.ok("This method must be authenticate with role admin");
    }

    @GetMapping("authentication-with-role-user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<String> authenticationWithAnyRole() {
        return ResponseEntity.ok("This method must be authentication with any role");
    }

}
