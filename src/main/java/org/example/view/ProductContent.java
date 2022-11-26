package org.example.view;

import org.example.model.Product;
import org.example.service.CartService;

import javax.swing.*;
import java.awt.*;

public class ProductContent extends JPanel {

    public String getProductName() {
        return productName;
    }

    private String productName;
    private JButton 장바구니추가Button;
    private JLabel imgField;
    private JLabel nameField;
    private JLabel priceField;
    private JPanel panel;

    CartService cartService;
    public void init(){
        cartService = CartService.getInstance();
    }

    public ProductContent(Product product,int i) {
        init();
        Dimension myDimension = new Dimension(150,200);
        productName = product.getName();
        add(panel);
        panel.setSize(200,200);
        panel.setMaximumSize(new Dimension(200,200));
        panel.setMinimumSize(myDimension);
        ImageIcon imageIcon = new ImageIcon("img/" + product.getImgPath());
        Image image = imageIcon.getImage();
        Image scaledInstance = image.getScaledInstance(150, 150, image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledInstance);

        imgField.setIcon(imageIcon);
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()) + "원");

        장바구니추가Button.setText("상품 상세");

        장바구니추가Button.addActionListener(e -> {
//            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            JFrame f4 = (JFrame) SwingUtilities.getRoot(this);
            f4.dispose();
            new ManagerProductDetailView(product);
        });
        setVisible(true);
    }
    public ProductContent(Product product, String userId) {
        init();
        Dimension myDimension = new Dimension(180,200);
        add(panel);
        panel.setSize(180,200);
        panel.setMaximumSize(new Dimension(180,200));
        panel.setMinimumSize(myDimension);

        ImageIcon imageIcon = new ImageIcon("img/"+ product.getImgPath());
        Image image = imageIcon.getImage();

        imgField.setIcon(imageIcon);
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()) + "원");
        장바구니추가Button.setVisible(false);

        setVisible(true);

    }

}
