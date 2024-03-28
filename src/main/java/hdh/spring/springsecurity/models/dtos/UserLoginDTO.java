package hdh.spring.springsecurity.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {

    @NotEmpty(message = "Username can't be empty!")
    @Size(min = 5, max = 100, message = "Username must be between 5 and 100!")
    private String username;

    @NotEmpty(message = "Password can't be empty!")
    private String password;
}
