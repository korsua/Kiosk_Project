package org.example.controller;

import org.example.view.CustomerHome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveToCustomerHome implements ActionListener {
    JFrame frame;
    String userId;

    public MoveToCustomerHome(Component root,String userId) {
        this.userId = userId;
        frame = (JFrame) root;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        new CustomerHome(this.userId);


    }
}
