package com.luxoft.alpha.intersango.domain;

import javax.persistence.*;

@Entity
@Table(name = "DEAL_TABLE")
public class Deal {

    @Id
    @GeneratedValue
    private Long id;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private DealState state = DealState.REQUESTED;

    @OneToOne
    private User acceptor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    public Deal() {
    }

    public Deal(Integer amount, User acceptor, Order order) {
        this.amount = amount;
        this.acceptor = acceptor;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public DealState getState() {
        return state;
    }

    public void setState(DealState state) {
        this.state = state;
    }

    public User getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(User acceptor) {
        this.acceptor = acceptor;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
