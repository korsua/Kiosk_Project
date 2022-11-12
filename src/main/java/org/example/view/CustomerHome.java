package org.example.view;

import javax.swing.*;

public class CustomerHome extends JFrame{
    private JPanel panel;
    private JLabel userName;
    private JTextField searchField;
    private JButton searchBtn;
    private JButton HelloHomeBtn;
    private JButton button1;

    public CustomerHome() {
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("회원페이지");
    }
}
