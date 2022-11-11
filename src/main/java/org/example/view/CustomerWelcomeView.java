package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerWelcomeView  extends JFrame {
    JButton loginBtn;
    JButton registerBtn;
    JPanel jPanel;
    public CustomerWelcomeView() {
        getContentPane();

        jPanel = new JPanel(new FlowLayout());

        add(jPanel);
//        loginView = new LoginView();
        loginBtn = new JButton("로그인");
        loginBtn.setPreferredSize(new Dimension(50,30));
        jPanel.add(loginBtn);
//        add(loginView);

        registerBtn = new JButton("회원가입");
        registerBtn.setPreferredSize(new Dimension(50,30));
        jPanel.add(registerBtn);


        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterUserView();
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                remove(jPanel);
//                add(loginView);
//                repaint();
//                revalidate();
                dispose();
                new LoginUserView();

            }
        });


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
//        new CustomerWelcomeView();
//        new LoginUserView();
        new ProductListView("test");

    }
}

