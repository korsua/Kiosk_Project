package org.example.repository;

import org.example.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductJdbcRepository implements ProductRepository{
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    static String url = "jdbc:mysql://localhost/dev";
    static String dbId = "root";
    static String dbPw = "1234";
    public ProductJdbcRepository(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbId, dbPw);
        } catch (ClassNotFoundException | SQLException e) {
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
    public int save(Product product) {
        int count;
        try {
            pstmt = conn.prepareStatement("insert into PRODUCT(name,price,imgPath) values(?,?,?)");
            pstmt.setString(1,product.getName());
            pstmt.setLong(2,product.getPrice());
            pstmt.setString(3,product.getImgPath());
            count = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    @Override
    public Product findByName(String name) {
        Product product = null;
        try {
            conn = DriverManager.getConnection(url, dbId, dbPw);
            pstmt = conn.prepareStatement("select * from PRODUCT where name = ?");
            pstmt.setString(1,name);
            rs = pstmt.executeQuery();

            if(rs.next()){
                product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
                product.setImgPath(rs.getString("imgPath"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return product;
    }


    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("select * from PRODUCT");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("productId"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
                product.setImgPath(rs.getString("imgPath"));
                list.add(product);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public int delete(String name) {
        int count = 0;
        try {
            pstmt = conn.prepareStatement("delete from PRODUCT where name=?");
            pstmt.setString(1,name);
            count = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return count;
    }

    @Override
    public Product findById(Long id) {
        Product product = null;
        try {
            pstmt = conn.prepareStatement("select * from PRODUCT where productId = ?");
            pstmt.setLong(1,id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                product = new Product();
                product.setId(rs.getLong("productId"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
                product.setImgPath(rs.getString("imgPath"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return product;
    }

    @Override
    public List<Product> matcherProductsByName(String text) {
        List<Product> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("select * from PRODUCT where name like ? ");
            pstmt.setString(1,"%"+text+"%");
            rs = pstmt.executeQuery();

            while(rs.next()){
                Product product = new Product();
                product.setId(rs.getLong("productId"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getLong("price"));
                product.setImgPath(rs.getString("imgPath"));
                list.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public void updateProduct(Product product) {
        try {
            pstmt = conn.prepareStatement("update PRODUCT set name=? , price=? , imgPath=? where productId=?");
            pstmt.setString(1, product.getName());
            pstmt.setLong(2,product.getPrice());
            pstmt.setString(3, product.getImgPath());
            pstmt.setLong(4,product.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
