package org.payLate.repository;

import org.payLate.entity.AmazonProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AmazonProductRepository extends BaseProductRepository<AmazonProduct> {
    Page<AmazonProduct> findByProductNameContaining(String productName, Pageable pageable);

    Page<AmazonProduct> findByCategory(String category, Pageable pageable);
}
