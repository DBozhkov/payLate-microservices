package org.payLate.repository;


import org.payLate.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseProductRepository<Product> {

    Page<Product> findByProductNameContaining(String productName, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

}
