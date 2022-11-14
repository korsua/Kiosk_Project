package org.example.view;

import org.example.model.Cart;
import org.example.model.Product;
import org.example.repository.CartJdbcRepository;
import org.example.repository.CartRepository;
import org.example.service.CartService;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerHome extends JFrame {
    private JPanel panel;
    private JTextField searchField;
    private JButton searchBtn;
    private JButton HelloHomeBtn;
    private JPanel productListPanel;
    private JLabel userIdLabel;
    private JPanel cartListPanel;
    private JScrollPane scrollpane;
    CartService cartService;

    private static long totalPrice = 0L;
    List<Product> products;

    ProductService productService;
    CartRepository cartRepository;
    private String userId;


    public CustomerHome(String userId) {
        cartService = CartService.getInstance();
        this.userId = userId;
        productService = ProductService.getInstance();
        cartRepository = new CartJdbcRepository();
        products = productService.findProducts();

        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));

        makeBtn();
        makeCartPanel();

        setContentPane(panel);


        userIdLabel.setText(userId);


        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("회원페이지");
        setVisible(true);
        HelloHomeBtn.addActionListener(e -> {
            dispose();
            new HelloHome();
        });
    }

    private void makeCartPanel() {
        totalPrice = 0L;
        cartListPanel.removeAll();
        List<Cart> all = cartRepository.findAllByUserId(userId);
        JPanel header = new JPanel(new FlowLayout());
        JButton deleteAllBtn = new JButton("모두 삭제");
        JButton payAll = new JButton("모두 결제");
        JLabel totalPriceLabel = new JLabel();
        header.add(deleteAllBtn);
        header.add(payAll);
        header.add(totalPriceLabel);
        cartListPanel.add(header);


        if (all != null)
            for(Cart cart : all){
                long cartSumPrice = cart.getSalePrice() * cart.getAmount();
                totalPrice += cartSumPrice;
                JPanel cartPanel = new JPanel(new FlowLayout());
                JButton minusBtn = new JButton("-");
                JButton plusBtn = new JButton("+");
                JLabel productName = new JLabel(productService.findProductById(cart.getProductId()).getName());
                JTextField countField = new JTextField(String.valueOf(cart.getAmount()));
                JTextField priceField = new JTextField(String.valueOf(cartSumPrice));
                cartPanel.add(minusBtn);
                cartPanel.add(plusBtn);
                cartPanel.add(productName);
                cartPanel.add(countField);
                cartPanel.add(priceField);

                cartListPanel.add(cartPanel);
                minusBtn.addActionListener(e ->{
                    countField.setText("3");
                });
            }
        totalPriceLabel.setText(String.valueOf(totalPrice));
        deleteAllBtn.addActionListener(e ->{
            cartRepository.deleteAllByUserId(userId);
            makeCartPanel();
        });
    }
    private void makeBtn() {
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        if (products != null) {
            for (Product product : products) {
                JButton productBtn = new JButton(product.getName());
                productListPanel.add(productBtn);

                productBtn.addActionListener(e -> {
//                    cartRepository.save(product, this.userId);
                    cartService.modifyCartCount(product, this.userId);

                    // 리턴이 1이면 원래있던것을 개수를 추가해준다.
                    makeCartPanel();
                    repaint();
                    revalidate();
                });
            }
        }
    }


    public static void main(String[] args) {
        new CustomerHome("test");
    }
}
