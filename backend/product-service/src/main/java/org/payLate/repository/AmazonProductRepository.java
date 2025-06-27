package org.payLate.repository;

import org.payLate.entity.AmazonProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "amazonProducts", path = "amazonProducts")
public interface AmazonProductRepository extends BaseProductRepository<AmazonProduct> {
    Page<AmazonProduct> findByProductNameContaining(@Param("productName") String productName, Pageable pageable);
    Page<AmazonProduct> findByCategory(@Param("category") String category, Pageable pageable);
}
