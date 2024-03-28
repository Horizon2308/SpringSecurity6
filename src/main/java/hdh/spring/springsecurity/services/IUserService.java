package hdh.spring.springsecurity.services;

import hdh.spring.springsecurity.exceptions.DataNotFoundException;
import hdh.spring.springsecurity.exceptions.InvalidParamException;
import hdh.spring.springsecurity.exceptions.UsernameOrPasswordIsInvalid;
import hdh.spring.springsecurity.models.dtos.UserDTO;
import hdh.spring.springsecurity.models.dtos.UserLoginDTO;
import hdh.spring.springsecurity.models.entities.User;

import java.util.Optional;

public interface IUserService {
    boolean existedUser(String username);
    User getUserByUsername(String username) throws DataNotFoundException;
    String createUser(UserDTO userDTO) throws DataNotFoundException, InvalidParamException;
    String validateUser(UserLoginDTO userLoginDTO) throws InvalidParamException, UsernameOrPasswordIsInvalid;
}
