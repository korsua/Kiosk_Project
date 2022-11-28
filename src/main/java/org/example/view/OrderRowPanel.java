package org.example.view;


import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderRowPanel extends JPanel {
    private JPanel panel;
    private JLabel 주문번호;
    private JLabel 상품품목;
    private JLabel 총가격;
    private JLabel 주문상태;
    private JButton clickBtn;
    static OrderDetailService orderDetailService = OrderDetailService.getInstance();
    static ProductService productService = ProductService.getInstance();

    OrderService orderService ;

    void init() {

    }

    private String statusMapper(int status) {
        if (status == 0) return "주문 확인";
        if (status == 1) return "진행중";
        return "주문완료";
    }

    public OrderRowPanel(Order order, String userId) {
        init();
        주문번호.setText(String.valueOf(order.getOrderId()));
        총가격.setText(String.valueOf(order.getTotalPrice()));
        clickBtn.setText("주문취소");
        List<OrderDetail> findedOrderDetail = orderDetailService.findAllByOrderId(order.getOrderId());

        StringBuilder builder = new StringBuilder();
        for (OrderDetail orderDetail : findedOrderDetail) {
            Product productById = productService.findProductById(orderDetail.getProductId());
            builder.append(productById.getName());
            builder.append(":");
            builder.append(orderDetail.getAmount());
            builder.append(",");
        }
        상품품목.setText(builder.toString());

        String statusString = statusMapper(order.getStatus());
        주문상태.setText(statusString);
        clickBtn.addActionListener(e ->{
            orderService =OrderService.getInstance();
            if(주문상태.getText().equals("주문 확인")){
                orderService.removeOrder(Integer.parseInt(주문번호.getText()));
            }
            JFrame f4 = (JFrame) SwingUtilities.getRoot(this);
            f4.dispose();
            new CustomerOrderPage(userId);


        });

        GridBagConstraints c = new GridBagConstraints();
        add(panel, c);
        setVisible(true);
    }
}
