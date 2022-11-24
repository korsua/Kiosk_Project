package org.example.controller;

import org.example.view.CustomerOrderPage;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MoveToCustomerOrderpage implements MouseListener {
    JFrame jframe;
    String userId;

    public MoveToCustomerOrderpage(JFrame jframe,String userId) {
        this.jframe = jframe;
        this.userId = userId;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jframe.dispose();
        new CustomerOrderPage(userId);


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
