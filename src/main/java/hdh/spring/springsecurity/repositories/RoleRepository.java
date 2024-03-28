package hdh.spring.springsecurity.repositories;

import hdh.spring.springsecurity.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
