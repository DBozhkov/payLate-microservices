package org.payLate.repository;

import org.payLate.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findByUserEmail(String userEmail);

    List<UserOrder> findByStatus(String status);
}
