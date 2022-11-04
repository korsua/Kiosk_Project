package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.jar.JarEntry;

public class CustomerWelcomeView  extends JFrame {
    JTextField searchField;
    JButton searchBtn;
    public CustomerWelcomeView(){
        Dimension dim = new Dimension(900, 500);

        JFrame frame = new JFrame("회원뷰");
        frame.setLocation(200,400);
        frame.setPreferredSize(dim);
        searchBtn = new JButton("검색");
        searchField = new JTextField();

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.X_AXIS));
        panel1.add(searchField);
        panel1.add(searchBtn);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2,2));
        panel2.add(new Button("라면 100원 추가하기"));
        panel2.add(new Button("라면 100원 추가하기"));
        panel2.add(new Button("라면 100원 추가하기"));
        panel2.add(new Button("라면 100원 추가하기"));

        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(1,1));
        panel3.add(panel1);
        panel3.add(panel2);


        frame.add(panel3);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CustomerWelcomeView();

    }
}
