package com.lv.Eshop.beans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column
    private String orderNum;

    @Column(nullable = false)
    private String userName;

//    @Column(nullable = false)
//    private int orderId;
//
//    @Column(nullable = false)
//    private int itemNum;
//
//    @Column(nullable = false)
//    private BigDecimal itemPrice;

    @Column
    private BigDecimal amount;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean paid;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean alive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    private List<SubOrder> subOrders;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public List<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<SubOrder> subOrders) {
        this.subOrders = subOrders;
    }
}
