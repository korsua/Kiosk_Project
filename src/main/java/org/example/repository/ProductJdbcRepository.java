package org.example.repository;

import org.example.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductJdbcRepository implements ProductRepository{
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;
    String url;
    String dbId;
    String dbPw;
    @Override
    public int save(String name, Long price) {
        int count;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/dev";
            dbId = "root";
            dbPw = "1234";
            conn = DriverManager.getConnection(url, dbId, dbPw);
            pstmt = conn.prepareStatement("insert into PRODUCT(name,price) values(?,?)");
            pstmt.setString(1,name);
            pstmt.setLong(2,price);
            count = pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public Product findByName(String name) {
        Product product = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/dev";
            dbId = "root";
            dbPw = "1234";
            conn = DriverManager.getConnection(url, dbId, dbPw);
            pstmt = conn.prepareStatement("select * from PRODUCT where name = ?");
            pstmt.setString(1,name);
            rs = pstmt.executeQuery();

            if(rs.next()){
                product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
            }

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }


    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/dev";
            dbId = "root";
            dbPw = "1234";
            conn = DriverManager.getConnection(url, dbId, dbPw);
            pstmt = conn.prepareStatement("select * from PRODUCT");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("hi");
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
                list.add(product);
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public int delete(String name) {
        int count = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/dev";
            dbId = "root";
            dbPw = "1234";
            conn = DriverManager.getConnection(url, dbId, dbPw);
            pstmt = conn.prepareStatement("delete from PRODUCT where name=?");
            pstmt.setString(1,name);
            count = pstmt.executeUpdate();



        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

}
