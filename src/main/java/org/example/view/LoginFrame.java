package org.example.view;

import org.example.model.User;
import org.example.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JButton submitBtn;
    private JTextField idField;
    private JPanel loginPanel;
    private JPasswordField pwField;
    private JButton registerBtn;
    private JLabel errorMessage;
    private UserService userService;

    public LoginFrame() {
        setContentPane(loginPanel);
//        add(loginPanel);
        userService = UserService.getInstance();

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        submitBtn.addActionListener(e -> {
            User user = null;
            try {
                user = userService.loginById(idField.getText());

                if (user != null) {
                    String pw = user.getUserPw();
                    Boolean role = user.getRole();
                    if (pw.equals(pwField.getText())) {
                        if (role == true) {
                            dispose();
                            new ManagerHome();
                        } else {
                            dispose();
                            new CustomerHome(user.getUserId());
                        }
                    } else {
                        JOptionPane.showConfirmDialog(null, "비밀번호 에러", "비밀번호가 틀렸습니다", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                errorMessage.setText("1");
                throw new RuntimeException(ex);
            } catch (IllegalStateException ex){
                errorMessage.setText(ex.getMessage());
                errorMessage.setForeground(Color.red);
                System.out.println("hi");
                ex.getStackTrace();
            }


        });
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
    }
}
