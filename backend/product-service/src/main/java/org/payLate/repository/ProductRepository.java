package org.payLate.repository;

import org.payLate.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends BaseProductRepository<Product> {

    Page<Product> findByProductNameContaining(@Param("productName") String productName, Pageable pageable);

    Page<Product> findByCategory(@Param("category") String category, Pageable pageable);
}