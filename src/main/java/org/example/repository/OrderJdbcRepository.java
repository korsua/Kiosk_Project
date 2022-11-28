package org.example.repository;

import org.example.model.Order;

import java.sql.*;
import java.time.LocalDate;
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
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(String userId, long totalPrice, String message) {
        long rowNum = 0;
        try {
            pstmt = conn.prepareStatement("insert into ORDERS(userId,totalPrice,requirements) values(?,?,?)");
            pstmt.setString(1, userId);
            pstmt.setLong(2, totalPrice);
            pstmt.setString(3, message);
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
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
    public List<Order> findAllByUserId(String userId,int currentPage) {
        List<Order> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("select * from ORDERS where userId =? order by status ASC ");
            pstmt.setString(1,userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
                order.setOrderId(rs.getLong("orderId"));
                order.setUserId(rs.getString("userId"));
                order.setStatus(rs.getInt("status"));
                order.setTotalPrice(rs.getLong("totalPrice"));
                order.setMessage(rs.getString("requirements"));
                System.out.println(order.getOrderId());
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return list;
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
                order.setMessage(rs.getString("requirements"));
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            pstmt.setLong(1, orderId);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("select status from ORDERS where orderId = ?");
            pstmt.setLong(1, orderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = (int) rs.getLong(1);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public String[][] findAllProductOrder(LocalDate start, LocalDate end, int index) {
        List<String[]> list = new ArrayList<>();
        try {
            String orderflag = "";
            switch (index){
                case 0 : orderflag ="productId"; break;
                case 1 : orderflag = "amount"; break;
                case 2 : orderflag = "totalPrice"; break;
            }
            pstmt = conn.prepareStatement(" select OD.productId,  SUM(OD.amount) amount,SUM(O.totalPrice) totalPrice from ORDER_DETAIL OD , ORDERS O where OD.orderId = O.orderId and"
                    + " DATE_FORMAT(regDate,'%Y-%m-%d') >= DATE_FORMAT(?,'%Y-%m-%d') and   DATE_FORMAT(regDate,'%Y-%m-%d') <= DATE_FORMAT(?,'%Y-%m-%d') group by OD.productId ORDER BY "+ orderflag +" DESC");
            pstmt.setString(1,start.toString());
            pstmt.setString(2,end.toString());
            System.out.println(orderflag);
            rs = pstmt.executeQuery();
            while(rs.next()){
                long productId = rs.getLong("productId");
                long amount = rs.getLong("amount");
                System.out.println(amount);
                long totalPrice = rs.getLong("totalPrice");
                list.add(new String[]{String.valueOf(productId),String.valueOf(amount),String.valueOf(totalPrice)});
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String[][] result = new String[list.size()][3];
        for(int i = 0 ; i< list.size();i++){
            result[i][0] = list.get(i)[0];
            result[i][1] = list.get(i)[1];
            result[i][2] = list.get(i)[2];
        }
        return result;
    }

    @Override
    public void deleteById(int orderId) {
        try {
            pstmt = conn.prepareStatement("delete from ORDER_DETAIL where orderId = ?");
            pstmt.setInt(1,orderId);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("delete from ORDERS where orderId = ?");
            pstmt.setInt(1,orderId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
