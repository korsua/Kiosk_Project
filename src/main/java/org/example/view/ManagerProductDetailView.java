package org.example.view;

import org.example.model.Product;
import org.example.service.ProductService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class ManagerProductDetailView extends JFrame{
    private JTextField productNameField;
    private JLabel ProductNameField;
    private JButton 수정하기Btn;
    private JButton 이미지선택Button;
    private JTextField productPriceField;
    private JButton 뒤로가기Button;
    private JPanel panel;
    private JLabel imgField;
    private JButton 삭제Button;
    String fullPath;
    String fileName;
    ProductService productService;
    JFileChooser fc;
    public ManagerProductDetailView(Product product) throws HeadlessException {
        productService = ProductService.getInstance();
        Product updateProduct = new Product();
        updateProduct.setId(product.getId());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setName(product.getName());
        updateProduct.setImgPath(product.getImgPath());
        setContentPane(panel);
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("상품 디테일 ");

        productNameField.setText(updateProduct.getName());
        productPriceField.setText(String.valueOf(updateProduct.getPrice()));

        ImageIcon imageIcon = new ImageIcon("img/"+ updateProduct.getImgPath());
        Image image = imageIcon.getImage();
        Image scaledInstance = image.getScaledInstance(150, 150, image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledInstance);
        fileName = updateProduct.getImgPath();
        imgField.setIcon(imageIcon);



        뒤로가기Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ManagerHome();
            }
        });
        이미지선택Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc = new JFileChooser();
                fc.showOpenDialog(panel);
                fullPath = String.valueOf(fc.getSelectedFile());
                fileName = fc.getSelectedFile().getName();
                ImageIcon imageIcon = new ImageIcon(fullPath);
                Image image = imageIcon.getImage();
                Image scaledInstance = image.getScaledInstance(150, 150, image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaledInstance);
                imgField.setIcon(imageIcon);

            }
        });
        수정하기Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("img/"+fileName);
                if (!file.exists()) {
                    try {
                        BufferedImage bImage = null;
                        File targetFile = new File(fullPath);
                        bImage = ImageIO.read(targetFile);
                        BufferedImage resizeImage = (BufferedImage) resizeToBig(bImage, 150, 150);
//                        ImageIO.write(resizeImage, "png", targetFile);
                        ImageIO.write(resizeImage, "png", new File("img/"+fileName));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                updateProduct.setPrice(Long.valueOf(productPriceField.getText()));
                updateProduct.setName(String.valueOf(productNameField.getText()));
                updateProduct.setImgPath(fileName);
                productService.modifyProduct(updateProduct);
                dispose();
                new ManagerHome();

            }
        });
        삭제Button.addActionListener(e ->{
            productService.deleteProduct(product);
            dispose();
            new ManagerHome();

        });
    }

    public static void main(String[] args) {
        Product product = new Product();
        product.setImgPath("flower.png");
        product.setName("hi");
        product.setPrice(1000L);
        product.setId(1L);
        new ManagerProductDetailView(product);
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
