package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;

import java.sql.*;
import java.util.List;

public class CartJdbcRepository implements CartRepository{
    @Override
    public void save(Product product, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

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
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/dev";
            String id = "root";
            String pw = "1234";

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
    public int updateById(Long cartId, int count) {
        return 0;
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
