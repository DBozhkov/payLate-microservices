package org.payLate.repository;

import org.payLate.entity.OlxProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "olxProducts", path = "olxProducts")
public interface OlxProductRepository extends BaseProductRepository<OlxProduct> {

    Page<OlxProduct> findByProductNameContaining(@Param("productName") String productName, Pageable pageable);

    Page<OlxProduct> findByCategory(@Param("category") String category, Pageable pageable);
}