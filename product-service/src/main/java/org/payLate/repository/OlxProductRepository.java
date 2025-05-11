package org.payLate.repository;


import org.payLate.entity.OlxProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OlxProductRepository extends BaseProductRepository<OlxProduct> {

    Page<OlxProduct> findByProductNameContaining(String productName, Pageable pageable);

    Page<OlxProduct> findByCategory(String category, Pageable pageable);
}
