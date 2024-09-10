package dev.marvin.repository;

import dev.marvin.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    @Query("SELECT r FROM RoleEntity r WHERE r.roleName = :roleName")
    Optional<RoleEntity> findByName(@Param("roleName") String roleName);
}
