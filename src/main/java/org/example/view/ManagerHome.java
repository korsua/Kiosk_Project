package org.example.view;

import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.network.ServerNet;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

public class ManagerHome extends JFrame{
    private JButton 상품등록Button;
    private JButton 주문현황Button;
    private JButton 상품Button;
    private JTextField searchField;
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


    public ManagerHome() {
//        ServerNet serverNet = new ServerNet();
        productService = ProductService.getInstance();
        orderService = OrderService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        userService = UserService.getInstance();
//        fileChooser = new JFileChooser();
        products = productService.findProducts();
//        JPanel panel1 = new JPanel();
//        panel1.setLayout(new FlowLayout());

        setContentPane(panel);
        makeProductBoard(products);

        setLocation(200, 200);
        setTitle("관리자 홈");
        setSize(950, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            String text = "";

            @Override
            public void insertUpdate(DocumentEvent e) {
                text = searchField.getText();
//                List<Product> matcherProducts = productService.findMatcherByName(text);
                List<Product> matcherProducts = new ArrayList<>();

                for (int i = 0; i < products.size(); i++) {
                    String pName = products.get(i).getName();
                    String pMatcher = "^.*" + text + ".*";
                    if (Pattern.matches(pMatcher, pName)) {
                        matcherProducts.add(products.get(i));
                    }
                }


                if (matcherProducts.size() == 0) {
                    makeProductBoard(null);
                }
                makeProductBoard(matcherProducts);


            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                text = searchField.getText();
                List<Product> matcherProducts = new ArrayList<>();

                for (int i = 0; i < products.size(); i++) {
                    String pName = products.get(i).getName();
                    String pMatcher = "^.*" + text + ".*";
                    if (Pattern.matches(pMatcher, pName)) {
                        matcherProducts.add(products.get(i));
                    }
                }


                if (matcherProducts.size() == 0) {
                    makeProductBoard(null);
                }
                makeProductBoard(matcherProducts);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
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
                    BufferedImage bImage = null;
                    File initialImage = new File(fullPath);
                    bImage = ImageIO.read(initialImage);
                    BufferedImage resizeImage = (BufferedImage) resizeToBig(bImage, 150, 150);
                    ImageIO.write(resizeImage, "png", targetFile);
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

    public void makeProductBoard(List<Product> products) {
        cardPanel.removeAll();
        cardPanel.add(productBoard);
        productHello.removeAll();
        productHello.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        int i = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        if (products != null)
            for (Product product : products) {
                if (i >= 5) {
                    i = 0;
                    c.gridy++;
                }
                ProductContent content = new ProductContent(product, 1);
                productHello.add(content, c);
                i++;
            }
        repaint();
        revalidate();
    }

    public void makeOrderBoard() {
        orderCard.removeAll();
        orderCard.setLayout(new BorderLayout());

        List<Order> orders = orderService.findOrders();
        String[] headerValue = {"주문아이디", "이름", "상태", "총 가격", "주문내용","요구사항"};
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
            tC.add(order.getMessage());

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

    private Image resizeToBig(Image originalImage, int biggerWidth, int biggerHeight) {
        int type = BufferedImage.TYPE_INT_ARGB;


        BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, this);
        g.dispose();


        return resizedImage;
    }
}
