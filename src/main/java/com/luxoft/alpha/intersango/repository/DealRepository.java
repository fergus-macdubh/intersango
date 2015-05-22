package com.luxoft.alpha.intersango.repository;

import com.luxoft.alpha.intersango.domain.Deal;
import com.luxoft.alpha.intersango.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query("from Deal where acceptor = :user")
    List<Deal> findDealsByAcceptor(@Param("user") User user);

    @Query("from Deal d where d.order.user = :user")
    List<Deal> findDealsByUserOrders(@Param("user") User user);

    @Query("from Deal d join fetch d.order where d.id = :id")
    Deal findOneWithOrder(@Param("id") Long id);
}
