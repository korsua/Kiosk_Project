package org.example.view;

import org.example.model.Cart;
import org.example.model.Product;
import org.example.repository.CartJdbcRepository;
import org.example.repository.CartRepository;
import org.example.service.CartService;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
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

    OrderService orderService;

    OrderDetailService orderDetailService;
    private String userId;
    List<Cart> all;


    public CustomerHome(String userId) {
        cartService = CartService.getInstance();
        this.userId = userId;
        productService = ProductService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        cartRepository = new CartJdbcRepository();
        orderService = OrderService.getInstance();
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
        all = cartRepository.findAllByUserId(userId);
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
                JLabel priceField = new JLabel(String.valueOf(cartSumPrice) + "원");
                cartPanel.add(minusBtn);
                cartPanel.add(plusBtn);
                cartPanel.add(productName);
                cartPanel.add(countField);
                cartPanel.add(priceField);

                cartListPanel.add(cartPanel);
                minusBtn.addActionListener(e ->{
                    cartRepository.updateById(cart, cart.getAmount() -1);
                    cart.setAmount(cart.getAmount() -1);
//                    countField.setText(String.valueOf(cart.getAmount() -1));
                    if(cart.getAmount() <= 0){
                        cartRepository.deleteByCartId(cart.getCartId());
                    }
                    makeCartPanel();
                });
                plusBtn.addActionListener(e ->{
                    cartRepository.updateById(cart, cart.getAmount() +1);
                    cart.setAmount(cart.getAmount() -1);
//                    countField.setText(String.valueOf(cart.getAmount() + 1));
//                    int targetPrice = Integer.parseInt(priceField.getText());
//                    targetPrice += cart.getSalePrice();
//                    priceField.setText(String.valueOf(targetPrice));
                    makeCartPanel();
                });
            }
        totalPriceLabel.setText(String.valueOf(totalPrice));
        deleteAllBtn.addActionListener(e ->{
            cartRepository.deleteAllByUserId(userId);
            makeCartPanel();
        });
        payAll.addActionListener(e ->{
            if(JOptionPane.showConfirmDialog(null, "주문을 하시겠습니까. ? ", "결제진행", JOptionPane.YES_NO_OPTION) != 1){
                long orderId = orderService.save(userId,totalPrice);
                orderDetailService.PayAll(all,orderId);
                cartRepository.deleteAllByUserId(userId);
                makeCartPanel();
            }else{
                JOptionPane.showConfirmDialog(null, "주문실패", "주문실패",JOptionPane.DEFAULT_OPTION);
            }

        });
    }
    private void makeBtn() {
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setPreferredSize(new Dimension(300,600));
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
