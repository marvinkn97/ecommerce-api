package dev.marvin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_roles")
@Data
public class Role {
    private Integer id;
    private String roleName;
}
