package org.payLate.repository;

import org.payLate.entity.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseProductRepository<T extends BaseProduct> extends JpaRepository<T, Long> {

}
