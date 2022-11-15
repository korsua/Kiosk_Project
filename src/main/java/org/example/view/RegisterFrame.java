package org.example.view;

import org.example.service.UserService;

import javax.swing.*;

public class RegisterFrame extends JFrame{
    private JButton submitBtn;
    private JTextField idField;
    private JPanel panel;
    private JPasswordField pwField;
    private JButton homeBtn;

    private UserService userService;

    public RegisterFrame() {
        userService = UserService.getInstance();

//        add(panel);
        setContentPane(panel);
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
        submitBtn.addActionListener(e -> {
            if(userService.join(idField.getText(), pwField.getText()) != null){
                JOptionPane.showConfirmDialog(null,"회원가입성공",String.format("%s 로 회원가입하였습니다",idField.getText()),JOptionPane.PLAIN_MESSAGE);
                dispose();
                new LoginFrame();
            }
        });

        homeBtn.addActionListener(e ->{
            dispose();
            new LoginFrame();
        });
    }
}
