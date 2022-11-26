package org.example.view;

import org.example.controller.MoveToCustomerOrderpage;
import org.example.model.Cart;
import org.example.model.Product;
import org.example.repository.CartJdbcRepository;
import org.example.repository.CartRepository;
import org.example.service.CartService;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
import org.example.service.ProductService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerHome extends JFrame {
    private JPanel panel;
    private JTextField searchField;
    private JPanel productListPanel;
    private JLabel userIdLabel;
    private JPanel cartListPanel;
    private JScrollPane scrollpane;
    private JPanel footerPanel;
    private JPanel searchPanel;
    private JPanel headerPanel;
    private JButton deleteAllBtn;
    private JButton payAllBtn;
    private JTextArea requestMessageField;
    CartService cartService;

    private static long totalPrice = 0L;
    List<Product> products;

    ProductService productService;
    CartRepository cartRepository;

    OrderService orderService;

    OrderDetailService orderDetailService;
    public static String userId;
    List<Cart> all;

    private void init() {
        cartService = CartService.getInstance();
        productService = ProductService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        cartRepository = new CartJdbcRepository();
        orderService = OrderService.getInstance();
        products = productService.findProducts();
        List<ProductContent> productContentList = new ArrayList<>();
        for(Product product : products){
        }
    }

    public CustomerHome(String userId) {
        this.userId = userId;
        userIdLabel.setText(this.userId);
        init();
        setContentPane(panel);

        makeHeaderPanel();
        makeProductPanel(products);
        makeCartPanel();

        makeFooterPanel();
        setSize(900, 1000);
        setMinimumSize(new Dimension(900, 1000));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("회원페이지");
        setVisible(true);
        userIdLabel.addMouseListener(new MoveToCustomerOrderpage((JFrame) SwingUtilities.getRoot(this), userIdLabel.getText()));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            String text = "";

            @Override
            public void insertUpdate(DocumentEvent e) {
                text = searchField.getText();
                List<Product> matcherProducts = new ArrayList<>();
                // productService.findMatcherByName(text); 느림 .
                // "*"+text+"*"
                long start = System.currentTimeMillis();
                for (int i = 0; i < products.size(); i++) {
                    String pName = products.get(i).getName();
                    String pMatcher = "^.*" + text + ".*";
                    if (Pattern.matches(pMatcher, pName)) {
                        matcherProducts.add(products.get(i));
                    }
                }
                System.out.println(System.currentTimeMillis() - start + "MS UPDATE");

                if (matcherProducts.size() == 0) {
                    makeProductPanel(null);
                }
                makeProductPanel(matcherProducts);


            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                text = searchField.getText();
                List<Product> matcherProducts = new ArrayList<>();
                for (int i = 0; i < products.size(); i++) {
                    String pName = products.get(i).getName();
                    String pMatcher = "^.*" + text + ".*";
                    if (Pattern.matches(pMatcher, pName)) {
                        matcherProducts.add(products.get(i));
                    }
                }

                if (matcherProducts.size() == 0) {
                    makeProductPanel(null);
                }
                makeProductPanel(matcherProducts);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }


    private void makeProductPanel(List<Product> productList) {
        long start = System.currentTimeMillis();
        productListPanel.removeAll();
        int i = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        if (productList != null) {
            for (Product product : productList) {
                if (i >= 5) {
                    i = 0;
                    c.gridy++;

                }
                long astart = System.currentTimeMillis();
                ProductContent productContent = new ProductContent(product, userId);
                System.out.println(System.currentTimeMillis() - astart + "MS / addProductPanel");
                productListPanel.add(productContent, c);
                productContent.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("hello");
                        cartService.modifyCartCount(product, userId);
                        makeCartPanel();
                        repaint();
                        revalidate();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        productContent.setBackground(Color.BLUE);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        productContent.setBackground(Color.GRAY);

                    }
                });
                i++;
            }
        }
        productListPanel.revalidate();
        productListPanel.repaint();
        System.out.println(System.currentTimeMillis() - start + "MS / makeProductPanel");
    }

    private void makeHeaderPanel() {
        userIdLabel.setText(userId);
    }

    private void makeCartPanel() {
        totalPrice = 0L;
        cartListPanel.removeAll();
        all = cartRepository.findAllByUserId(userId);


        if (all != null)
            for (Cart cart : all) {
                long cartSumPrice = cart.getSalePrice() * cart.getAmount();
                totalPrice += cartSumPrice;
                JPanel cartPanel = new JPanel(new GridBagLayout());
                JLabel productName = new JLabel(productService.findProductById(cart.getProductId()).getName());
                JButton minusBtn = new JButton("-");
                JButton plusBtn = new JButton("+");
                JTextField countField = new JTextField(String.valueOf(cart.getAmount()));
                JLabel priceField = new JLabel(cartSumPrice + "원");
                GridBagConstraints top = new GridBagConstraints();
                top.gridy = 0;
                top.gridwidth = 3;
                GridBagConstraints center = new GridBagConstraints();
                center.gridy = 1;
                center.gridwidth = 1;
                GridBagConstraints bottom = new GridBagConstraints();
                bottom.gridy = 2;
                bottom.gridwidth = 3;

                cartPanel.add(productName, top);
                cartPanel.add(minusBtn, center);
                cartPanel.add(countField, center);
                cartPanel.add(plusBtn, center);
                cartPanel.add(priceField, bottom);
                cartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cartListPanel.add(cartPanel);
                minusBtn.addActionListener(e -> {
                    cartRepository.updateById(cart, cart.getAmount() - 1);
                    cart.setAmount(cart.getAmount() - 1);
                    if (cart.getAmount() <= 0) {
                        cartRepository.deleteByCartId(cart.getCartId());
                    }
                    makeCartPanel();
                });
                plusBtn.addActionListener(e -> {
                    cartRepository.updateById(cart, cart.getAmount() + 1);
                    cart.setAmount(cart.getAmount() + 1);
                    makeCartPanel();
                });
                repaint();
                revalidate();
                payAllBtn.setText(totalPrice + "원 모두결제");
            }
    }

    private void makeFooterPanel() {
        payAllBtn.setText(totalPrice + "원 모두결제");
        payAllBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "주문을 하시겠습니까. ? ", "결제진행", JOptionPane.YES_NO_OPTION) != 1) {
                long orderId = orderService.save(userId, totalPrice);
                orderDetailService.PayAll(all, orderId);
                cartRepository.deleteAllByUserId(userId);
                makeCartPanel();
                payAllBtn.setText("모두결제");
                repaint();
                revalidate();
            } else {
                JOptionPane.showConfirmDialog(null, "주문실패", "주문실패", JOptionPane.DEFAULT_OPTION);
            }
        });
        deleteAllBtn.addActionListener(e -> {
            cartRepository.deleteAllByUserId(userId);
            makeCartPanel();
            repaint();
            revalidate();
        });
    }


    public static void main(String[] args) {
        new CustomerHome("qotndk0530");
    }
}
