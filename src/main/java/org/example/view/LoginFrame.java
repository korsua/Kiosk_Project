package org.example.view;

import org.example.service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame{
    private JButton button1;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel loginPanel;

    public LoginFrame() {
//        Container ct = getContentPane();
//        ct.add(loginPanel);
        setContentPane(loginPanel);
        setTitle("Welcome");
        setSize(450,300);
        setVisible(true);

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("바뀜");
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserService service = new UserService();
                String userId = textField1.getText();
                String userPw = textField2.getText();
                service.join(userId,userPw);
                setVisible(false);
//                if(service.loginById(userId).getUserPw().equals(userPw)){
//                    textField1.setText("성공하셨습니다.");
//                    setVisible(false);
//                }else{
//                    textField1.setText("실패하셨습니다.");
//                    setVisible(false);
//                }

            }
        });
    }


}
