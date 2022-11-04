package org.example.service;

import org.example.model.User;

import java.sql.*;

public class UserJdbcRepository implements UserRepository{

    @Override
    public User save(String userId, String userPw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

            conn = DriverManager.getConnection(url, id, pw);

            pstmt = conn.prepareStatement("insert into USER values(?,?,0)");
            pstmt.setString(1,userId);
            pstmt.setString(2,userPw);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User findById(String userId) {
        User user = new User();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

            conn = DriverManager.getConnection(url, id, pw);

            pstmt = conn.prepareStatement("select userPw, role from USER where userId = ?");
            pstmt.setString(1,userId);
            rs = pstmt.executeQuery();


            while(rs.next()){
                String userPw = rs.getString(1);
                Boolean role = rs.getBoolean(2);
                user.setUserId(userId);
                user.setUserPw(userPw);
                user.setRole(role);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
