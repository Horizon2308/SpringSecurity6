package hdh.spring.springsecurity.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDTO {

    @NotEmpty(message = "Username can't be empty!")
    @Size(min = 5, max = 100, message = "Username must be between 5 to 100")
    @JsonProperty("username")
    private String username;

    @NotEmpty(message = "Password can't be empty!")
    private String password;

    @NotEmpty(message = "Full name can't be empty")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Retype password can't be empty!")
    @JsonProperty("retype_password")
    private String retypePassword;

    @NotEmpty(message = "Email can't be empty!")
    @JsonProperty("email")
    private String email;

    private Long roleId;

}
