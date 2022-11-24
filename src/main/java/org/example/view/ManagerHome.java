package org.example.view;

import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ManagerHome extends JFrame {
    private JButton 상품등록Button;
    private JButton 주문현황Button;
    private JButton 상품Button;
    private JTextField textField1;
    private JButton button1;
    private JPanel productBoard;
    private JPanel cardPanel;
    private JPanel panel;
    private JPanel addProduct;
    private JPanel orderCard;
    private JTextField nameField;
    private JTextField priceField;
    private JButton 등록;
    private JLabel productNameLabel;
    private JLabel productPriceLabel;
    private JPanel productHello;
    private JPanel searchHeader;
    private JScrollPane myScrollPane;
    private JButton 파일추가;
    private JLabel fileInfo;

    ProductService productService;
    OrderService orderService;
    OrderDetailService orderDetailService;
    UserService userService;
    JFileChooser fc;
    private String fullPath;
    private String fileName;
    List<Product> products;

    //    private JFileChooser fileChooser;
    public void makeProductBoard(List<Product> products) {
        productHello.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        int i = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        if (products != null)
            for (Product product : products) {
                if( i >= 5){
                    i = 0;
                    c.gridy++;
                }
//            JButton productLabel = new JButton(product.getName() + " " + product.getPrice());
                ProductContent content = new ProductContent(product, 1);
                productHello.add(content, c);
                i++;
            }
    }
    public ManagerHome() {
        productService = ProductService.getInstance();
        orderService = OrderService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        userService = UserService.getInstance();
//        fileChooser = new JFileChooser();
        products = productService.findProducts();
//        JPanel panel1 = new JPanel();
//        panel1.setLayout(new FlowLayout());

        setContentPane(panel);

        setLocation(200, 200);
        setTitle("관리자 홈");
        setSize(950, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        textField1.getDocument().addDocumentListener(new DocumentListener() {
            String text = "";

            @Override
            public void insertUpdate(DocumentEvent e) {
                text = textField1.getText();
                List<Product> matcherProducts = productService.findMatcherByName(text);

                if (matcherProducts.size() == 0) {
                    makeProductBoard(null);
                }
                makeProductBoard(matcherProducts);


            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                text = textField1.getText();
                List<Product> matcherProducts = productService.findMatcherByName(text);

                if (matcherProducts.size() == 0) {
                    makeProductBoard(null);
                }
                makeProductBoard(matcherProducts);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                text = textField1.getText();
                List<Product> matcherProducts = productService.findMatcherByName(text);

                if (matcherProducts.size() == 0) {
                    makeProductBoard(null);
                }
                makeProductBoard(matcherProducts);

            }
        });

        상품Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.removeAll();
                cardPanel.add(productBoard);
                productHello.removeAll();
                makeProductBoard(products);
                repaint();
                revalidate();

            }
        });
        주문현황Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.removeAll();
                cardPanel.add(orderCard);
                makeOrderBoard();
                repaint();
                revalidate();
            }
        });
        상품등록Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.removeAll();
                cardPanel.add(addProduct);
                repaint();
                revalidate();

            }
        });
        등록.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product product = new Product();
                product.setName(nameField.getText());
                product.setPrice(Long.valueOf(priceField.getText()));
                product.setImgPath(fileName);
                File targetFile = new File(fullPath);
                try {
                    productService.saveProduct(product);
                    FileInputStream inputStream = new FileInputStream(targetFile);
                    FileOutputStream outputStream = new FileOutputStream("img/" + fileName);
                    byte[] b = new byte[1024];
                    int s = 0;
                    while ((s = inputStream.read(b)) != -1) {
                        outputStream.write(b, 0, s);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                products = productService.findProducts();
            }
        });
        파일추가.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                fc = new JFileChooser();
                fc.showOpenDialog(panel);
                fullPath = String.valueOf(fc.getSelectedFile());
                fileName = String.valueOf(fc.getSelectedFile().getName());
                fileInfo.setText(fileName);
            }
        });
    }


    public void makeOrderBoard() {
        orderCard.removeAll();
        orderCard.setLayout(new BorderLayout());

        List<Order> orders = orderService.findOrders();
        String[] headerValue = {"주문아이디", "이름", "상태", "총 가격", "주문내용"};
        Vector<Object> tHeader = new Vector<>();
        tHeader.addAll(Arrays.asList(headerValue));

        //2 차원 배열
        Vector<Vector<Object>> tContent = new Vector<>();

        JPanel panel = new JPanel();
        for (Order order : orders) {
            StringBuilder builder = new StringBuilder();
            List<OrderDetail> findedOrderDetail = orderDetailService.findAllByOrderId(order.getOrderId());
            for (OrderDetail orderDetail : findedOrderDetail) {
                Product productById = productService.findProductById(orderDetail.getProductId());
                builder.append(productById.getName());
                builder.append(":");
                builder.append(orderDetail.getAmount());
                builder.append(",");
            }

            // 이친구를 2차원 배열안에 넣을거야
            JButton statusBtn = new JButton(String.valueOf(order.getStatus()));
            Vector<Object> tC = new Vector<>();
            tC.add(String.valueOf(order.getOrderId()));
//            tC.add(String.valueOf(order.getRegDate()));
            tC.add(String.valueOf(order.getUserId()));
//            tC.add(statusBtn);
            tC.add(String.valueOf(order.getStatus()));
            tC.add(String.valueOf(order.getTotalPrice()));
            tC.add(builder.toString());

            tContent.add(tC);


            JLabel orderLabel = new JLabel(order.getRegDate() + "" + order.getUserId() + " " + order.getStatus() + " " + order.getTotalPrice());
            panel.add(orderLabel);
        }

        JTable table = new JTable(tContent, tHeader);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(4).setPreferredWidth(400);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //table returned OBJECT **
                Long selectedOrderId = Long.parseLong((String) table.getValueAt(table.getSelectedRow(), 0));
                int i = 0;
                if ((i = orderService.processOrderByOrderId(selectedOrderId)) >= 2) {
//                    table.removeRo(table.getSelectedRow());
                    ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                    getRootPane().repaint();
                    getRootPane().revalidate();
                } else {
                    table.setValueAt(i, table.getSelectedRow(), 2);
                }

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
        });
        JScrollPane scrollPane = new JScrollPane(table);
//        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
//        table.setSize(myScrollPane.getSize());
//        myScrollPane.getViewport().add(table);
//        myScrollPane.setBackground(Color.BLUE);
//        GridBagConstraints top = new GridBagConstraints();
//        top.gridy = 0;
//        top.gridx = 0;
////        top.gridwidth = 1;
////        top.gridheight =1;
//        top.weightx =1;
//        top.weighty = 1;
//        top.fill = GridBagConstraints.HORIZONTAL;
//        top.fill = GridBagConstraints.VERTICAL;

//        orderCard.add(myScrollPane);

//        orderCard.add(scrollPane,top);
        orderCard.add(scrollPane, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        new ManagerHome();
    }
}
