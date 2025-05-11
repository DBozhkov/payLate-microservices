package org.payLate.repository;


import org.payLate.entity.AliExpressProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AliExpressProductRepository extends BaseProductRepository<AliExpressProduct> {

    Page<AliExpressProduct> findByProductNameContaining(String productName, Pageable pageable);

    Page<AliExpressProduct> findByCategory(String category, Pageable pageable);
}
