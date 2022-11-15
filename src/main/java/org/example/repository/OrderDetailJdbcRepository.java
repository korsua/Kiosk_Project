package org.example.repository;

import org.example.model.Cart;

import java.sql.*;

public class OrderDetailJdbcRepository implements OrderDetailRepository{
    final static String url = "jdbc:mysql://localhost/dev";
    final static String id = "root";
    final static String pw = "1234";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @Override
    public void save(long orderId, Cart cart) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("insert into ORDER_DETAIL(orderId,productId,amount) values(?,?,?)");
            pstmt.setLong(1,orderId);
            pstmt.setLong(2,cart.getProductId());
            pstmt.setLong(3,cart.getAmount());
            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
