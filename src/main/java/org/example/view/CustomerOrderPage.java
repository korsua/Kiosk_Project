package org.example.view;

import org.example.controller.MoveToCustomerHome;
import org.example.model.Order;
import org.example.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderPage extends JFrame{

    private JButton 뒤로가기;
    private JPanel panel;
    private JLabel 주문번호;
    private JLabel 상품품목;
    private JLabel 총가격;
    private JLabel 주문상태;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JButton 이전Btn;
    private JButton 이후Btn;
    private JLabel 페이지숫자;
    private String userId;
    GridBagConstraints c;

    private int currentPage = 0;
    OrderService orderService = null;

    private List<Order> ordersById;

    void init(){
        currentPage = 0;
        orderService = OrderService.getInstance();
        ordersById = orderService.findOrdersById(userId,currentPage);
    }
    public CustomerOrderPage(String userId) {

        this.userId = userId;
        init();
        setContentPane(panel);
        c = new GridBagConstraints();
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;

        뒤로가기.addActionListener(new MoveToCustomerHome(SwingUtilities.getRoot(this),userId));

        makeOrderWithUserIdListPanel(ordersById, userId,currentPage);

        int maxRow = (int) Math.ceil(ordersById.size() / 5.0f);

        setSize(900, 1000);
        setMinimumSize(new Dimension(900,1000));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("회원주문확인페이지");
        setVisible(true);

        이전Btn.addActionListener(e->{
            if (currentPage - 1 >= 0) {
                currentPage -= 1;
                contentPanel.removeAll();

                페이지숫자.setText(String.valueOf(currentPage));
                makeOrderWithUserIdListPanel(ordersById,userId,currentPage);
                repaint();
                revalidate();
            }

        });
        이후Btn.addActionListener(e ->{
            System.out.println("click");
            if (currentPage <  maxRow-1) {
                System.out.println("no");
                currentPage += 1;
                contentPanel.removeAll();

                페이지숫자.setText(String.valueOf(currentPage));
                makeOrderWithUserIdListPanel(ordersById,userId,currentPage);
                repaint();
                revalidate();
            }

        });
    }

    private void makeOrderWithUserIdListPanel(List<Order> ordersById, String userId,int currentPage) {
//        if(orders.size() == 0){
//            throw new IllegalStateException("더이상없습니다");
//        }

        int i = 1;
        List<Order> orders = new ArrayList<>();
        for(int j = currentPage * 5, len = currentPage * 5; j< len + 5;j++){
            if(ordersById.size() > j){
                orders.add(ordersById.get(j));
            }
        }
        for(Order order : orders){
            OrderRowPanel orderRowPanel = new OrderRowPanel(order);
            layout(orderRowPanel,0,i++,1,1);
        }
    }

    void layout(Component object, int x, int y ,int width,int height){
        c.gridx=x; // 시작위치 x
        c.gridy=y; // 시작위치 y
        c.gridwidth=width; // 컨테이너 너비
        c.gridheight=height;  // 컨테이너 높이
        contentPanel.add(object,c);
    }

    public static void main(String[] args) {
        new CustomerOrderPage("qotndk0530");
    }
}
