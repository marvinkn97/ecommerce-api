package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_categories")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String categoryName;
    @Enumerated(EnumType.STRING)
    private Status status;
}
