package com.lv.Eshop.beans;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cartId")
    private List<SubCart> subCarts;

    @OneToOne(mappedBy = "shoppingCart")
    private User user;

    public List<SubCart> getSubCarts() {
        return subCarts;
    }

    public void setSubCarts(List<SubCart> subCarts) {
        this.subCarts = subCarts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
