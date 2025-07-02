package org.payLate.repository;


import org.payLate.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProductId(@RequestParam("product_id") Long productId,
                                 Pageable pageable);

    Review findByUserEmailAndProductId(String userEmail, Long productId);

    @Modifying
    @Query("delete from Review r where r.productId = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}