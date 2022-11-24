package org.example.repository;

import org.example.model.Cart;
import org.example.model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailJdbcRepository implements OrderDetailRepository {
    final static String url = "jdbc:mysql://localhost/dev";
    final static String id = "root";
    final static String pw = "1234";
    final static String driver = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public OrderDetailJdbcRepository() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, id, pw);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(long orderId, Cart cart) {
        try {
            pstmt = conn.prepareStatement("insert into ORDER_DETAIL(orderId,productId,amount) values(?,?,?)");
            pstmt.setLong(1, orderId);
            pstmt.setLong(2, cart.getProductId());
            pstmt.setLong(3, cart.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(rs != null ) rs.close();
                if(pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<OrderDetail> findAllbyOrderId(long orderId) {
        List<OrderDetail> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("select * from ORDER_DETAIL where orderId = ?");
            pstmt.setLong(1, orderId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                long productId = rs.getLong("productId");
                long amount = rs.getLong("amount");
                orderDetail.setAmount(amount);
                orderDetail.setProductId(productId);

                list.add(orderDetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}
