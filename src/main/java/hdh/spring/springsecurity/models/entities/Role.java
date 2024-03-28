package hdh.spring.springsecurity.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
@Entity
public class Role {

    @Id
    private Long id;

    @Column(length = 100)
    private String name;
}
