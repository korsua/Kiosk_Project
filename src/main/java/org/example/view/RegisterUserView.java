package org.example.view;

import org.example.model.User;
import org.example.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUserView extends JFrame {
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

        getContentPane();
        setLayout(new GridLayout(3,1));
        headerPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
        message = new JLabel("hi");

        idPanel = new JPanel();
        idLabel = new JLabel("id : ");
        idTextFiled = new JTextField();

        pwPanel = new JPanel(new FlowLayout());
        pwLabel = new JLabel("pw : ");
        pwTextFiled = new JTextField();

        footerPanel = new JPanel(new FlowLayout());
        confirm = new JButton("확인");


        idTextFiled.setPreferredSize(new Dimension(150, 50));
        pwTextFiled.setPreferredSize(new Dimension(150, 50));
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

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = userService.loginById(idTextFiled.getText());
                if(user != null){
                    String pw = user.getUserPw();
                    if (pw.equals(pwTextFiled.getText())) {
                        dispose();
                        new CustomerWelcomeView();

                    }
                    else{
                        message.setText("비밀번호가 틀렸습니다.");
                    }
                }

            }
        });

        setSize(500, 1500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
}
