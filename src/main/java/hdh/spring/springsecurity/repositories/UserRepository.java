package hdh.spring.springsecurity.repositories;

import hdh.spring.springsecurity.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);
    Optional<User> findUserByUsername(String username);
}
