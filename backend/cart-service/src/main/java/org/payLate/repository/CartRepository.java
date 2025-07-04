package org.payLate.repository;


import org.payLate.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmail(String userEmail);

    @Query("SELECT c FROM Cart c WHERE LOWER(TRIM(c.userEmail)) = LOWER(TRIM(:userEmail))")
    Optional<Cart> findByUserEmailIgnoreCase(@Param("userEmail") String userEmail);
}