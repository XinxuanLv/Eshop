package com.lv.Eshop.VoType;

import com.lv.Eshop.beans.SubOrder;

import java.math.BigDecimal;
import java.util.List;

public class OrderVO {
    private String oderNum;
    private String userName;
    private BigDecimal amount;
    private boolean paid;
    private boolean alive;
    private List<SubOrderVO> subOrderVOList;

    public String getOderNum() {
        return oderNum;
    }

    public void setOderNum(String oderNum) {
        this.oderNum = oderNum;
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<SubOrderVO> getSubOrderVOList() {
        return subOrderVOList;
    }

    public void setSubOrderVOList(List<SubOrderVO> subOrderVOList) {
        this.subOrderVOList = subOrderVOList;
    }
}
