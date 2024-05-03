package com.example.ordering_lecture.order.repository;

import com.example.ordering_lecture.order.entity.Ordering;
import com.example.ordering_lecture.orderdetail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Ordering,Long> {
    List<Ordering> findAllByEmail(String email);
    @Query("SELECT od FROM OrderDetail od  JOIN od.ordering o WHERE od.sellerId = :sellerId")
    List<OrderDetail> findAllBySeller(@Param("sellerId") Long sellerId);
}
