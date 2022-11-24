package org.example.service;

import org.example.model.Cart;
import org.example.model.OrderDetail;
import org.example.repository.OrderDetailJdbcRepository;
import org.example.repository.OrderDetailRepository;

import java.util.List;

public class OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    static OrderDetailService instance;
    private OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }
    public static OrderDetailService getInstance() {
        if(instance == null) instance = new OrderDetailService(new OrderDetailJdbcRepository());
        return instance;
    }
    public int PayAll(List<Cart> all, long orderId){
        for(Cart cart : all){
            orderDetailRepository.save(orderId, cart);
        }
        return 1;
    }
    public List<OrderDetail> findAllByOrderId(long orderId) {
        return orderDetailRepository.findAllbyOrderId(orderId);
    }
}
