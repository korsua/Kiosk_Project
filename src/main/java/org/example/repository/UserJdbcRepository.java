package org.example.repository;

import org.example.model.User;

import java.sql.*;

public class UserJdbcRepository implements UserRepository{

    @Override
    public User save(String userId, String userPw) {
        User user = new User();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

            conn = DriverManager.getConnection(url, id, pw);
//            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("insert into USER values(?,?,0)");
//            Savepoint savepoint1 = conn.setSavepoint("SavePoint1");
            pstmt.setString(1,userId);
            pstmt.setString(2,userPw);
            pstmt.executeUpdate();

            user.setUserId(userId);
            user.setUserPw(userPw);
//            conn.commit();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
//            conn.rollback();
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User findById(String userId) throws SQLException, IllegalStateException {
        User user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

            conn = DriverManager.getConnection(url, id, pw);

            //sql exception 여기서 일어나니까
            pstmt = conn.prepareStatement("select userPw, role from USER where userId = ?");
            pstmt.setString(1,userId);
            rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();


            user = new User();
            while(rs.next()){
                String userPw = rs.getString(1);
                Boolean role = rs.getBoolean(2);
                user.setUserId(userId);
                user.setUserPw(userPw);
                user.setRole(role);
            }
            if(user.getUserId() == null){
                user = null;
                throw new IllegalStateException("아이디없음");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
