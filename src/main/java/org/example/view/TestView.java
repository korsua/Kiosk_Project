package org.example.view;

import org.example.model.Product;
import org.example.service.ProductService;

import javax.swing.*;
import java.util.List;

public class TestView extends JFrame{
    List<Product> products;
    ProductService productService;

    private JPanel panel;
    private JButton button1;
    private JPanel mainP;

    public TestView(){
        productService = ProductService.getInstance();
        products = productService.findProducts();
        createUIComponents();

        add(mainP);

        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Text View");
        setVisible(true);
    }


    public static void main(String[] args) {
        new TestView();
    }

    private void createUIComponents() {
//        List<Product> products1 = ProductService.getInstance().findProducts();
//        panel = new JPanel();
//        if (products1 != null) {
//            for (Product product : products1) {
//                panel.add(new JButton(product.getName() + product.getPrice()));
//            }
//        }
        panel = new JPanel();
        System.out.println();
//        List<Product> products = ProductService.getInstance().findProducts();
        if (products != null) {
            for (Product product : products) {
                panel.add(new JButton(product.getName() + product.getPrice()));
            }
        }
    }
}
