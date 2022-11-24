package org.example.view;

import org.example.controller.MoveToCustomerHome;

import javax.swing.*;
import java.awt.*;

public class CustomerOrderPage extends JFrame{

    private JButton 뒤로가기;
    private JPanel panel;
    private String userId;

    public CustomerOrderPage(String userId) {
        this.userId = userId;
        setContentPane(panel);

        뒤로가기.addActionListener(new MoveToCustomerHome(SwingUtilities.getRoot(this),userId));



        setSize(900, 1000);
        setMinimumSize(new Dimension(900,1000));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("회원주문확인페이지");
        setVisible(true);
    }
}
