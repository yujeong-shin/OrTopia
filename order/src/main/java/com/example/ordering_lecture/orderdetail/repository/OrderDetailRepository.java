package com.example.ordering_lecture.orderdetail.repository;

import com.example.ordering_lecture.orderdetail.dto.*;
import com.example.ordering_lecture.orderdetail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findAllByOrderingId(Long orderId);

    @Query("SELECT new com.example.ordering_lecture.orderdetail.dto.BuyerGraphPriceData(DATE(o.createdTime) as createdTime, SUM(od.discountPrice) as price) FROM OrderDetail od JOIN od.ordering o WHERE od.statue = 'COMPLETE_DELIVERY' AND od.createdTime BETWEEN :startDate AND :endDate AND o.email = :email GROUP BY DATE(o.createdTime)\n")
    List<BuyerGraphPriceData> findSumPriceByDateBetweenAndStatueAndEmail(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("email") String email);

    @Query("SELECT new com.example.ordering_lecture.orderdetail.dto.BuyerGraphCountData(DATE(o.createdTime) as createdTime, COUNT(*) as count) FROM OrderDetail od JOIN od.ordering o WHERE od.statue = 'COMPLETE_DELIVERY' AND o.createdTime BETWEEN :startDate AND :endDate AND o.email = :email GROUP BY DATE(od.createdTime)")
    List<BuyerGraphCountData> findCompletedOrdersByEmailAndDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("email") String email);

    @Query("SELECT new com.example.ordering_lecture.orderdetail.dto.SellerGraphPriceData(DATE(od.createdTime) as createdTime, SUM(od.discountPrice) as price) FROM OrderDetail od WHERE od.statue = 'COMPLETE_DELIVERY' AND od.sellerId = :sellerId AND od.createdTime BETWEEN :startDate AND :endDate GROUP BY DATE(od.createdTime)")
    List<SellerGraphPriceData> findSalesData(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("sellerId") Long sellerId);

    @Query("SELECT new com.example.ordering_lecture.orderdetail.dto.SellerGraphCountData(DATE(od.createdTime) as createdTime, COUNT(*) as count) FROM OrderDetail od WHERE od.statue = 'COMPLETE_DELIVERY' AND od.sellerId = :sellerId AND od.createdTime BETWEEN :startDate AND :endDate GROUP BY DATE(od.createdTime)")
    List<SellerGraphCountData> findSalesDataBySellerIdAndDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("sellerId") Long sellerId);

    @Query("SELECT od.itemId, SUM(od.discountPrice) AS totalDiscountPrice FROM OrderDetail od WHERE od.sellerId = :sellerId AND od.statue = 'COMPLETE_DELIVERY' AND od.createdTime BETWEEN :startDate AND :endDate GROUP BY od.itemId")
    List<Object[]> getItemPriceDataBySellerId(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("sellerId") Long sellerId);}
