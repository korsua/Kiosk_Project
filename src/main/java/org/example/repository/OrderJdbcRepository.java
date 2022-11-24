package org.example.repository;

import org.example.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderJdbcRepository implements OrderRepository {
    final static String url = "jdbc:mysql://localhost/dev";
    final static String id = "root";
    final static String pw = "1234";
    final static String driver = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public OrderJdbcRepository() {
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
    public void close() {
        try {
            if(conn != null) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(String userId, long totalPrice) {
        long rowNum = 0;
        try {
            pstmt = conn.prepareStatement("insert into ORDERS(userId,totalPrice) values(?,?)");
            pstmt.setString(1, userId);
            pstmt.setLong(2, totalPrice);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("select last_insert_id() from ORDERS LIMIT 1");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rowNum = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(rs!=null)rs.close();
                if(pstmt != null) pstmt.close();
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

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("select * from ORDERS where status < 2");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
                order.setOrderId(rs.getLong("orderId"));
                order.setUserId(rs.getString("userId"));
                order.setStatus(rs.getInt("status"));
                order.setTotalPrice(rs.getLong("totalPrice"));
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(rs !=null) rs.close();
                if(pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return list;
    }

    @Override
    public int updateStatusByOrderId(long orderId) {
        int result = 0;
        try {
            pstmt = conn.prepareStatement("update ORDERS set status = status + 1 where orderId = ?");
            pstmt.setLong(1,orderId);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("select status from ORDERS where orderId = ?");
            pstmt.setLong(1,orderId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println(result);
                result = (int) rs.getLong(1);
                System.out.println(result);
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
        return result;
    }
}
