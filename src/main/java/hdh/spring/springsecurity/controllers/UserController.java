package hdh.spring.springsecurity.controllers;

import hdh.spring.springsecurity.exceptions.DataNotFoundException;
import hdh.spring.springsecurity.exceptions.InvalidParamException;
import hdh.spring.springsecurity.exceptions.UsernameOrPasswordIsInvalid;
import hdh.spring.springsecurity.http_responses.ErrorResponse;
import hdh.spring.springsecurity.models.dtos.UserDTO;
import hdh.spring.springsecurity.models.dtos.UserLoginDTO;
import hdh.spring.springsecurity.models.responses.AuthenticationResponse;
import hdh.spring.springsecurity.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/user")
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO,
                                           BindingResult result) {
        if (result.hasErrors()) {
            List<String> messages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorResponse<>(messages));
        }
        try {
            String token = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticationResponse.builder()
                            .time(LocalDateTime.now())
                            .token(token)
                            .message("Created successfully!")
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse<>(e.getMessage()));
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse<>(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                   BindingResult result) {
        if (result.hasErrors()) {
            List<String> messages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorResponse<>(messages));
        }
        try {
            String token = userService.validateUser(userLoginDTO);
            return token != null ? ResponseEntity.ok(AuthenticationResponse.builder()
                            .time(LocalDateTime.now())
                            .message("Login successfully!")
                            .token(token)
                    .build()) : ResponseEntity.badRequest().body(new ErrorResponse<>("Username or password is invalid"));
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse<>(e.getMessage()));
        } catch (UsernameOrPasswordIsInvalid e) {
            return ResponseEntity.badRequest().body(new ErrorResponse<>(e.getMessage()));
        }
    }
}
