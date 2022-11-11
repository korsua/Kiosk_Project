package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JPanel {
    JLabel text;
    JTextField idTextField;
    JTextField passWordTextField;
    JButton confirmBtn;

    public LoginView() {
        setBackground(Color.BLUE);
//        getContentPane();

        text = new JLabel();
        idTextField = new JTextField();
        passWordTextField = new JTextField();

        confirmBtn = new JButton("로그인");

        add(passWordTextField);
        add(idTextField);
        add(text);
        add(confirmBtn);
        passWordTextField.setPreferredSize(new Dimension(100,50));

        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passWordTextField.setText("확인");
            }
        });

        setSize(1000,500);
        setVisible(true);
    }

}
