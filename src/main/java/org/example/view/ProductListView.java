package org.example.view;

import org.example.model.Product;
import org.example.repository.CartJdbcRepository;
import org.example.repository.CartRepository;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ProductListView extends JFrame {
    JPanel headerPanel;
    JTextField searchTextField;
    JButton searchBtn;
    String userId;


    JPanel productListPanel;
    JPanel contentPanel;
    JPanel cartPanel;

    ProductService service;
    CartRepository cartRepository;

    public ProductListView(String userId) throws HeadlessException {

        this.userId = userId;

        Container ct = getContentPane();
        cartRepository = new CartJdbcRepository();
        service = ProductService.getInstance();

        setLayout(new BoxLayout(ct,BoxLayout.Y_AXIS));

        headerPanel = new JPanel();

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(500,50));
        searchTextField.setText(userId + "반갑습니다.");

        searchBtn = new JButton("검색");

        headerPanel.add(searchTextField);
        headerPanel.add(searchBtn);

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product productByName = service.findProductByName(searchTextField.getText());
                System.out.println(productByName.getName());
            }
        });

        contentPanel = new JPanel(new FlowLayout());


        productListPanel = new JPanel(new GridLayout(2,2,10,10));
        productListPanel.setSize(900,500);
        List<Product> products = service.findProducts();
        for(Product product : products){
            JPanel panel = new JPanel();
            JButton productBtn = new JButton(product.getName() + product.getPrice());
            productBtn.setPreferredSize(new Dimension(150,150));
            productBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        cartRepository.save(product,userId);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            panel.add(productBtn);
            productListPanel.add(panel);
        }
        cartPanel = new JPanel();
        JTextArea cartTextArea = new JTextArea();
        cartTextArea.setPreferredSize(new Dimension(600,500));
        cartPanel.add(cartTextArea);
        cartPanel.setSize(600,500);

        contentPanel.add(productListPanel);
        contentPanel.add(cartPanel);

        add(headerPanel);
        add(contentPanel);


        setSize(1500,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
