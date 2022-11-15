package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CartJdbcRepository implements CartRepository{
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    final static String url = "jdbc:mysql://localhost/dev";
    final static String id = "root";
    final static String pw = "1234";
    @Override
    public void save(Product product, String userId) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("insert into CART(userId,productId,price) values(?,?,?)");
            pstmt.setString(1,userId);
            pstmt.setLong(2,product.getId());
            pstmt.setLong(3, product.getPrice());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cart findById(Long cartId) {
        Cart cart = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("select * from CART WEHRE cartId = ?");
            pstmt.setLong(1,cartId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cart = new Cart();
                String userId = rs.getString("userId");
                Long productId = rs.getLong("productId");
                Long amount = rs.getLong("amount");
                Long saleprice = rs.getLong("price");
                cart.setCartId(cartId);
                cart.setProductId(productId);
                cart.setSalePrice(saleprice);
                cart.setUserId(userId);
                cart.setAmount(amount);
            }

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    public int updateById(Cart cart, long count) {
        int i = 0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("update CART " +
                    "set amount = ? where cartId = ? ");
            pstmt.setLong(1,count);
            pstmt.setLong(2,cart.getCartId());
            i = pstmt.executeUpdate();


            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return i;
    }


    @Override
    public List<Cart> findAllByUserId(String userId) {
        List<Cart> carts = new ArrayList<>();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("select * from CART WHERE userId = ?");
            pstmt.setString(1,userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cart cart = new Cart();
                cart.setAmount(rs.getLong("amount"));
                cart.setUserId(userId);
                cart.setProductId(rs.getLong("productId"));
                cart.setSalePrice(rs.getLong("price"));
                cart.setCartId(rs.getLong("cartId"));
                carts.add(cart);
            }

            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carts;
    }

    @Override
    public int deleteAllByUserId(String userId) {
        int count = 0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, id, pw);
            pstmt = conn.prepareStatement("delete from CART where userId = ?");
            pstmt.setString(1,userId);
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
    public Cart existCart(Long productId, String userId) {
        Cart cart = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,id,pw);
            pstmt = conn.prepareStatement("select * from CART where userId = ? and productId = ?");
            pstmt.setString(1,userId);
            pstmt.setLong(2,productId);
            rs = pstmt.executeQuery();

            if(rs.next()){
                cart = new Cart();
                cart.setUserId(rs.getString("userId"));
                cart.setAmount(rs.getLong("amount"));
                cart.setSalePrice(rs.getLong("price"));
                cart.setProductId(rs.getLong("productId"));
                cart.setCartId(rs.getLong("cartId"));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public int deleteByCartId(long cartId) {
        int i;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,id,pw);
            pstmt = conn.prepareStatement("delete from CART where cartId = ?");
            pstmt.setLong(1,cartId);
            i = pstmt.executeUpdate();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return i;
    }
}
