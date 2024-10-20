package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tbl_addresses")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String streetName;
    private String buildingName;
    private String state;
    private String country;
    private String pinCode;
}
