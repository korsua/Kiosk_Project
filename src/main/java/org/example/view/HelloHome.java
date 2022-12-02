package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloHome extends JFrame{
    private JButton loginBtn;
    private JButton registerBtn;
    private JPanel homePanel;

    public HelloHome(){
//        add(homePanel);
        setContentPane(homePanel);
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterFrame();
            }
        });
    }

    public static void main(String[] args) {
        new LoginFrame();
        new LoginFrame();
        new ManagerHome();
    }
}
