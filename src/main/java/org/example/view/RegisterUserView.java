package org.example.view;

import org.example.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUserView extends JFrame {
    JButton homeBtn;
    JPanel headerPanel;
    JPanel footerPanel;
    JLabel idLabel;
    JTextField idTextFiled;
    JPanel idPanel;
    JPanel pwPanel;
    JLabel pwLabel;
    JTextField pwTextFiled;
    JButton confirm;
    JLabel message;
    private UserService userService;

    public RegisterUserView() throws HeadlessException {
        userService = new UserService();

        Container ct = getContentPane();
        setLayout(new BoxLayout(ct, BoxLayout.Y_AXIS));
        headerPanel = new JPanel(new FlowLayout());
        message = new JLabel("hi Register Page");

        idPanel = new JPanel();
        idLabel = new JLabel("id : ");
        idTextFiled = new JTextField();

        pwPanel = new JPanel(new FlowLayout());
        pwLabel = new JLabel("pw : ");
        pwTextFiled = new JTextField();

        footerPanel = new JPanel(new FlowLayout());
        confirm = new JButton("확인");
        homeBtn = new JButton("홈");


        idTextFiled.setPreferredSize(new Dimension(250, 50));
        pwTextFiled.setPreferredSize(new Dimension(250, 50));
//        message.setPreferredSize(new Dimension(50,50));

        message.setForeground(Color.red);
        add(headerPanel);
        add(idPanel);
        add(pwPanel);
        add(footerPanel);


        headerPanel.add(message);
        idPanel.add(idLabel);
        idPanel.add(idTextFiled);
        pwPanel.add(pwLabel);
        pwPanel.add(pwTextFiled);
        footerPanel.add(confirm);
        footerPanel.add(homeBtn);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userService.join(idTextFiled.getText(), pwTextFiled.getText()) != null){
                    dispose();
                    new LoginUserView();
                }

            }
        });
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerWelcomeView();
            }
        });

        setSize(500, 1500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
}
