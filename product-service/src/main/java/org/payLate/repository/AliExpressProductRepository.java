package org.payLate.repository;

import org.payLate.entity.AliExpressProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "aliExpressProducts", path = "aliExpressProducts")
public interface AliExpressProductRepository extends BaseProductRepository<AliExpressProduct> {

    Page<AliExpressProduct> findByProductNameContaining(@Param("productName") String productName, Pageable pageable);

    Page<AliExpressProduct> findByCategory(@Param("category") String category, Pageable pageable);
}