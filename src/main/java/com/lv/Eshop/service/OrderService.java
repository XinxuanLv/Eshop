package com.lv.Eshop.service;

import com.lv.Eshop.VoType.OrderVO;
import com.lv.Eshop.VoType.SubOrderVO;
import com.lv.Eshop.beans.*;
import com.lv.Eshop.dao.*;
import com.lv.Eshop.http.Response;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class OrderService {
    @Autowired
    UserDao userDao;

    @Autowired
    GoodsDao goodsDao;
    ;

    @Autowired
    SequenceDao sequenceDao;

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    SubOrderDao subOrderDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    SubCartDao subCartDao;

    public Response createOrder(Authentication authentication) {
        String userName = authentication.getName();
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return new Response(false, "No such user/ wrong user type");
        }
        ShoppingCart shoppingCart = shoppingCartDao.findById(user.getShoppingCart().getId());
        List<SubCart> cartList = shoppingCart.getSubCarts();

        Order order = new Order();
        order.setUserName(userName);
        String OrderNum = generateOrderNum();

        order.setOrderNum(OrderNum);
        ordersDao.save(order);

        List<SubOrder> subOrderList = new ArrayList<SubOrder>();
        BigDecimal amount = new BigDecimal("0");
        if (cartList.size() <= 0){
            return new Response(false,"No Item in the cart");
        }

        for (int i = 0; i < cartList.size(); i++) {
            int productId = cartList.get(i).getProductId();
            int num = cartList.get(i).getProductNum();
            Goods goods = goodsDao.findById(productId);
            if (goods == null) {
                return new Response(false, "No such product");
            }
            if (goods.getStock() <= 0 || num > goods.getStock()) {
                return new Response(false, "no Stock");
            }

            //decrease stock
            goods.setStock(goods.getStock() - num);
            goodsDao.save(goods);

            SubOrder subOrder = new SubOrder();
            subOrder.setProductId(productId);
            subOrder.setProductNum(num);
            subOrder.setProductPrice(goods.getPrice());
            subOrder.setOrder(order);

            subOrderList.add(subOrder);
            subOrderDao.save(subOrder);
            amount = amount.add(goods.getPrice().multiply(BigDecimal.valueOf(num)));
        }
        order.setSubOrders(subOrderList);
        order.setAmount(amount);
        order.setAlive(true);
        order.setPaid(false);
        ordersDao.save(order);
        //delete the cart
        subCartDao.deleteByCartId(shoppingCart);
        cartList.clear();
        shoppingCart.setSubCarts(cartList);
        return new Response(true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNum() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowTime = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowTime);

        int sequence = 0;
        SequenceInfo sequenceInfo = sequenceDao.findByName("order_info");
        sequence = sequenceInfo.getCurrentValue();
        sequenceInfo.setCurrentValue(sequenceInfo.getCurrentValue() + sequenceInfo.getStep());
        sequenceDao.save(sequenceInfo);
        String seqStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - seqStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(seqStr);
        return stringBuilder.toString();
    }

    public Response payOrder(String orderNum, Authentication authentication) {
        Order order = ordersDao.findByOrderNum(orderNum);
        if (!order.getUserName().equals(authentication.getName())) {
            return new Response(false, "You cannot change other's order");
        }
        if (order == null) {
            return new Response(false, "No such order");
        }
        if (!order.isAlive()) {
            return new Response(false, "Order already canceled");
        }
        if (order.isPaid()) {
            return new Response(false, "Order already paid");
        }
        order.setPaid(true);
        ordersDao.save(order);
        return new Response(true);
    }

    public Response cancelOrder(String orderNum, Authentication authentication) {
        Order order = ordersDao.findByOrderNum(orderNum);
        if (order == null) {
            return new Response(false, "No such order");
        }
        if (!order.getUserName().equals(authentication.getName())) {
            return new Response(false, "You cannot change other's order");
        }
        order.setAlive(false);
        ordersDao.save(order);
        return new Response(true);
    }

    public List<OrderVO> list(){
        List<Order> orderList= ordersDao.findAll();
        List<OrderVO> orderVOList = new ArrayList<OrderVO>();
        for (int i = 0; i < orderList.size(); i ++){
            OrderVO orderVO = new OrderVO();
            orderVO.setOderNum(orderList.get(i).getOrderNum());
            orderVO.setUserName(orderList.get(i).getUserName());
            orderVO.setAlive(orderList.get(i).isAlive());
            orderVO.setPaid(orderList.get(i).isPaid());
            orderVO.setAmount(orderList.get(i).getAmount());
            List<SubOrder> subOrderList = orderList.get(i).getSubOrders();
            List<SubOrderVO> subOrderVOList = new ArrayList<SubOrderVO>();
            for (int j = 0; j < subOrderList.size(); j ++){
                SubOrderVO subOrderVO = new SubOrderVO();
                subOrderVO.setProductId(subOrderList.get(j).getProductId());
                subOrderVO.setProductNum(subOrderList.get(j).getProductNum());
                subOrderVO.setProductPrice(subOrderList.get(j).getProductPrice());
                subOrderVOList.add(subOrderVO);
            }
            orderVO.setSubOrderVOList(subOrderVOList);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    public List<OrderVO> listForUser(Authentication authentication){
        String userName = authentication.getName();
        List<Order> orderList= ordersDao.findByUserName(userName);
        List<OrderVO> orderVOList = new ArrayList<OrderVO>();
        for (int i = 0; i < orderList.size(); i ++){
            OrderVO orderVO = new OrderVO();
            orderVO.setOderNum(orderList.get(i).getOrderNum());
            orderVO.setUserName(orderList.get(i).getUserName());
            orderVO.setAlive(orderList.get(i).isAlive());
            orderVO.setPaid(orderList.get(i).isPaid());
            orderVO.setAmount(orderList.get(i).getAmount());
            List<SubOrder> subOrderList = orderList.get(i).getSubOrders();
            List<SubOrderVO> subOrderVOList = new ArrayList<SubOrderVO>();
            for (int j = 0; j < subOrderList.size(); j ++){
                SubOrderVO subOrderVO = new SubOrderVO();
                subOrderVO.setProductId(subOrderList.get(j).getProductId());
                subOrderVO.setProductNum(subOrderList.get(j).getProductNum());
                subOrderVO.setProductPrice(subOrderList.get(j).getProductPrice());
                subOrderVOList.add(subOrderVO);
            }
            orderVO.setSubOrderVOList(subOrderVOList);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }
}
