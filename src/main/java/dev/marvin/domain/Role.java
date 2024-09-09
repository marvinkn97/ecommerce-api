package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;
}
