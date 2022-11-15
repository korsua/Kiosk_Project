package org.example.repository;

import org.example.model.Order;

import java.sql.*;

public class OrderJdbcRepository implements OrderRepository{
    final static String url = "jdbc:mysql://localhost/dev";
    final static String id = "root";
    final static String pw = "1234";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @Override
    public long save(String userId, long totalPrice) {
        long rowNum = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("insert into ORDERS(userId,totalPrice) values(?,?)");
            pstmt.setString(1,userId);
            pstmt.setLong(2,totalPrice);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("select last_insert_id() from ORDERS LIMIT 1");
            rs = pstmt.executeQuery();
            if(rs.next()){
               rowNum =  rs.getLong(1);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //reviewing
        return rowNum;
    }

    @Override
    public Order updateByOrderId(Long orderId, String status) {
        return null;
    }

    @Override
    public Order findAllByUserId(String userId) {
        return null;
    }
}
