package dev.marvin.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c WHERE c.userEntity.id = :id")
    Optional<Cart> findByUserId(@Param("id") Integer userId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.userEntity.id = :id")
    void deleteCartByUser(@Param("id") Integer userId);
}
