package org.example.view;

import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.service.OrderDetailService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.jdesktop.swingx.JXDatePicker;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class ManagerHome extends JFrame{
    public Socket socket;
    private JButton 상품등록Button;
    private JButton 주문현황Button;
    private JButton 상품Button;
    private JTextField searchField;
    private JButton 검색Button;
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
    private JButton 상품매출Button;
    private JPanel searchOrderBoard;
    private JPanel searchOrderHeaderPanel;
    private JPanel searchProductOrderFiled;
    private JScrollPane searchScollPane;
    private JLabel totalPriceFFiled;

    ProductService productService;
    OrderService orderService;
    OrderDetailService orderDetailService;
    JFileChooser fc;
    UserService userService;
    private String fullPath;
    private String fileName;
    List<Product> products;



    public void startClient() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    socket = new Socket("localhost", 5505);
                    receive();

                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        stopClient();
                        System.out.println("[서버접속실패]");
                    }

                }
            }
        };
        //지울거
        thread.start();
    }

    public void stopClient() {
        try{
            if(socket != null && socket.isClosed() == false){
                socket.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void send(String message) {
        Thread thread = new Thread(){
            public void run(){
                try{
                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = message.getBytes("UTF-8");
                    out.write(buffer);
                    out.flush();

                }catch (Exception e){
                    stopClient();
                }
            }

        };
        thread.start();
    }

    public void receive() {
        while (true) {
            try{
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[512];
                int length = in.read(buffer);
                if(length == -1) throw new IOException();
                String message = new String(buffer, 0, length, "UTF-8");
                cardPanel.removeAll();
                cardPanel.add(orderCard);
                makeOrderBoard();

            } catch (IOException e) {
                stopClient();
                break;
            }
        }
    }
    public ManagerHome() {
        startClient();

        productService = ProductService.getInstance();
        orderService = OrderService.getInstance();
        orderDetailService = OrderDetailService.getInstance();
        userService = UserService.getInstance();
        products = productService.findProducts();

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

        상품Button.addActionListener(e -> {
            cardPanel.removeAll();
            cardPanel.add(productBoard);
            makeProductBoard(products);
        });
        주문현황Button.addActionListener(e -> {
            cardPanel.removeAll();
            cardPanel.add(orderCard);
            makeOrderBoard();
        });
        상품등록Button.addActionListener(e -> {
            cardPanel.removeAll();
            cardPanel.add(addProduct);
            repaint();
            revalidate();

        });
        상품매출Button.addActionListener(e ->{
            cardPanel.removeAll();
            cardPanel.add(searchOrderBoard);
            makeSearchOrderBoard();
        });
        등록.addActionListener(e -> {
            Product product = new Product();
            product.setName(nameField.getText());
            product.setPrice(Long.valueOf(priceField.getText()));
            product.setImgPath(fileName);
            System.out.println(fullPath);
            File targetFile = new File(fullPath);
            try {
                productService.saveProduct(product);
                BufferedImage bImage = null;
                File initialImage = new File(fullPath);
                bImage = ImageIO.read(initialImage);
                BufferedImage resizeImage = (BufferedImage) resizeToBig(bImage, 150, 150);
                ImageIO.write(resizeImage, "png", targetFile);
                ImageIO.write(resizeImage, "png", new File("img/"+fileName));
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
        });
        파일추가.addActionListener(e -> {
            fc = new JFileChooser();
            fc.showOpenDialog(panel);
            fullPath = String.valueOf(fc.getSelectedFile());
            fileName = String.valueOf(fc.getSelectedFile().getName());
            fileInfo.setText(fileName);
        });
    }

    private void makeSearchOrderBoard() {
        searchProductOrderFiled.setLayout(new BorderLayout());
        searchOrderHeaderPanel.removeAll();
        JXDatePicker startDate = new JXDatePicker();
        JXDatePicker endDate = new JXDatePicker();
        JButton searchButton = new JButton("submit");
        String[] choices = { "이름","판매량", "총 금액"};
        JComboBox<String> comboBox = new JComboBox<>(choices);
        startDate.setFormats("yyyy-MM-dd");
        endDate.setFormats("yyyy-MM-dd");
        searchOrderHeaderPanel.add(comboBox);
        searchOrderHeaderPanel.add(startDate);
        searchOrderHeaderPanel.add(new JLabel("-"));
        searchOrderHeaderPanel.add(endDate);
        searchOrderHeaderPanel.add(searchButton);
        searchButton.addActionListener(e ->{
            Vector<Object> tHeader = new Vector<>();
            Vector<Vector<Object>> tContent = new Vector<>();
            searchProductOrderFiled.removeAll();
            Date date1 = startDate.getDate();
            Date date2 = endDate.getDate();

            LocalDate start = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int selectedIndex = comboBox.getSelectedIndex();
            String[] header = {"상품이름","개수","가격"};
            String[][] strings = orderService.requestProductOrder(start, end, selectedIndex);
            tHeader.addAll(Arrays.asList(header));

            int totalPrice = 0;
            for(String[] str : strings){
//                tC.addAll(Arrays.asList(str));
                Vector<Object> tC = new Vector<>();
                Product productById = productService.findProductById(Long.parseLong(str[0]));

                tC.add(productById.getName());
                tC.add(str[1]);
                tC.add(str[2]);
                totalPrice += Integer.parseInt(str[2]);
                tContent.add(tC);
            }
            JTable table = new JTable(tContent,tHeader);
            JScrollPane scrollPane = new JScrollPane(table);
            searchProductOrderFiled.add(scrollPane,BorderLayout.CENTER);
            totalPriceFFiled.setText(String.valueOf(totalPrice)+" 원");
            repaint();
            revalidate();
        });
        repaint();
        revalidate();
    }

    public void makeProductBoard(List<Product> products) {
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
        productHello.repaint();
        productHello.revalidate();
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
            tC.add(String.valueOf(order.getUserId()));
            String c_status = statusMapper(order.getStatus());
            tC.add(c_status);
            tC.add(String.valueOf(order.getTotalPrice()));
            tC.add(builder.toString());
            tC.add(order.getMessage());

            tContent.add(tC);
            tContent.stream().forEach(System.out::println);


            JLabel orderLabel = new JLabel(order.getRegDate() + "" + order.getUserId() + " " + order.getStatus() + " " + order.getTotalPrice());
            panel.add(orderLabel);
        }

        JTable table = new JTable(tContent, tHeader);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(4).setPreferredWidth(400);
        table.getColumnModel().getColumn(5).setPreferredWidth(400);
        table.setRowHeight(30);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Long selectedOrderId = Long.parseLong((String) table.getValueAt(table.getSelectedRow(), 0));
                int i = 0;
                if ((i = orderService.processOrderByOrderId(selectedOrderId)) >= 2) {
                    ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                    getRootPane().repaint();
                    getRootPane().revalidate();
                } else {
                    String c_status = statusMapper(i);
                    table.setValueAt(c_status, table.getSelectedRow(), 2);
//                    makeOrderBoard();
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
        orderCard.add(scrollPane, BorderLayout.CENTER);
        repaint();
        revalidate();

    }

    private String statusMapper(int status) {
        if(status == 0 ) return "주문 확인";
        if(status == 1 ) return "진행중";
        return "주문완료";
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
