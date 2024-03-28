package hdh.spring.springsecurity.services;

import hdh.spring.springsecurity.models.dtos.RoleDTO;
import hdh.spring.springsecurity.models.entities.Role;

public interface IRoleService {
    Role createRole(RoleDTO roleDTO);
    void deleteRole(Long id);
}
