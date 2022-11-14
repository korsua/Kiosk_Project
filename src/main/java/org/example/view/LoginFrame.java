package org.example.view;

import org.example.model.User;
import org.example.service.UserService;

import javax.swing.*;

public class LoginFrame extends JFrame{
    private JButton submitBtn;
    private JTextField idField;
    private JPanel loginPanel;
    private JPasswordField pwField;
    private JButton homeBtn;
    private UserService userService;

    public LoginFrame() {
        setContentPane(loginPanel);
//        add(loginPanel);
        userService = UserService.getInstance();

        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        submitBtn.addActionListener(e -> {
            User user = userService.loginById(idField.getText());
            if(user != null){
                String pw = user.getUserPw();
                if (pw.equals(pwField.getText())) {
                    dispose();
                    new CustomerHome(user.getUserId());
                }
                else{
                    JOptionPane.showConfirmDialog(null,"비밀번호 에러","비밀번호가 틀렸습니다",JOptionPane.PLAIN_MESSAGE);
                }
            }

        });
        homeBtn.addActionListener((event) ->{
            dispose();
            new HelloHome();
        });
    }
}
