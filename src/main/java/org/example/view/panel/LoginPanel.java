package org.example.view.panel;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    JLabel idLabel;
    JTextField idTextFiled;
    JPanel idPanel;
    public LoginPanel() {
        idLabel = new JLabel("id : ");
        idTextFiled = new JTextField();

        idTextFiled.setPreferredSize(new Dimension(150,50));

        add(idPanel);

        idPanel.add(idLabel);
        idPanel.add(idTextFiled);

        setSize(1000,500);
        setVisible(true);

    }
}
