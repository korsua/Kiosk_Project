package org.example.controller;

import org.example.view.HelloHome;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeMoveController extends JFrame implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new HelloHome();

    }
}
