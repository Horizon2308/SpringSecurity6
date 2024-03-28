package hdh.spring.springsecurity.services;

import hdh.spring.springsecurity.models.dtos.RoleDTO;
import hdh.spring.springsecurity.models.entities.Role;
import hdh.spring.springsecurity.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(RoleDTO roleDTO) {
        return roleRepository.save(Role.builder()
                        .id(roleDTO.getId())
                        .name(roleDTO.getName())
                .build());
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}