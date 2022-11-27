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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerHome extends JFrame {
    public Socket socket = null;
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

    public void startClient() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    socket = new Socket("localhost", 5505);
                    receive();

                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        stopClient();
                        System.out.println("[서버접속실패]");
                    }

                }
            }
        };
        //지울거
        thread.start();
    }

    public void stopClient() {
        try{
            if(socket != null && socket.isClosed() == false){
                socket.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void send(String message) {
        Thread thread = new Thread(){
            public void run(){
                try{
                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = message.getBytes("UTF-8");
                    out.write(buffer);
                    out.flush();

                }catch (Exception e){
                    stopClient();
                }
            }

        };
        thread.start();
    }

    public void receive() {
        while (true) {
            try{
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[512];
                int length = in.read(buffer);
                if(length == -1) throw new IOException();
                String message = new String(buffer, 0, length, "UTF-8");
                System.out.println(message);

            } catch (IOException e) {
                stopClient();
                break;
            }
        }
    }


    private void init() {
        cartService = CartService.getInstance();
        productService = ProductService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        cartRepository = new CartJdbcRepository();
        orderService = OrderService.getInstance();
        products = productService.findProducts();
        startClient();
        send("clinent meesage" +
                "");
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
        setSize(100, 1000);
        setMinimumSize(new Dimension(1000, 1000));
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
                ProductContent productContent = new ProductContent(product, userId);
                productListPanel.add(productContent, c);
                productContent.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
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
                String message = requestMessageField.getText();
                requestMessageField.setText("");
                long orderId = orderService.save(userId, totalPrice, message);
                orderDetailService.PayAll(all, orderId);
                cartRepository.deleteAllByUserId(userId);
                makeCartPanel();
                payAllBtn.setText("모두결제");
                send("hi");
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
