package com.luxoft.alpha.intersango.domain;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Integer amount;

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.OPEN;

    private Date createdDate = new Date();

    private Boolean isPartialAllowed;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Deal> deals;

    @ManyToOne
    private User user;

    public Order(OrderType type, Currency currency, Integer amount, Double price, User user) {
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.price = price;
        this.user = user;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsPartialAllowed() {
        return isPartialAllowed;
    }

    public void setIsPartialAllowed(Boolean isPartialAllowed) {
        this.isPartialAllowed = isPartialAllowed;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }
}
