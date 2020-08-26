package com.lv.Eshop.VoType;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    private List<ProductVO> productVOList;
//    private BigDecimal amount;

    public List<ProductVO> getProductVOList() {
        return productVOList;
    }

    public void setProductVOList(List<ProductVO> productVOList) {
        this.productVOList = productVOList;
    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
}
