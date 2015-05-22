package com.luxoft.alpha.intersango.repository;

import com.luxoft.alpha.intersango.domain.Currency;
import com.luxoft.alpha.intersango.domain.Order;
import com.luxoft.alpha.intersango.domain.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("from Order o where o.currency = :currency and o.state = :state")
    List<Order> findOrdersByCurrencyAndState(
            @Param("currency") Currency currency,
            @Param("state") OrderState state);

    @Query("from Order o left join fetch o.deals where o.id = :id")
    Order findOneWithDeals(@Param("id") Long id);

    @Query("from Order o where o.state = :state")
    List<Order> findOrdersByState(@Param("state") OrderState state);
}
