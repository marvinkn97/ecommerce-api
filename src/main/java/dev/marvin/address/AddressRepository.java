package dev.marvin.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.userEntity.id = :userId")
    Optional<Address> findByUserId(@Param("userId") Integer userId);
}
