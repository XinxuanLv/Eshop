package com.lv.Eshop.beans;

import javax.persistence.*;

@Entity
@Table(name = "subcart")
public class SubCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "product_num", nullable = false)
    private int productNum;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart cartId;

    public SubCart(){
        super();
    }

    public SubCart(int id) {
        super();
        this.id = id;
    }

    public ShoppingCart getCart() {
        return cartId;
    }

    public void setCart(ShoppingCart cart) {
        this.cartId = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    @Override
    public String toString() {
        return "SubCart{" +
                "id=" + id +
                ", productId=" + productId +
                ", productNum=" + productNum +
                '}';
    }
}
