package org.payLate.services;

import org.payLate.entity.OrderItem;
import org.payLate.entity.UserOrder;
import org.payLate.repository.UserOrderRepository;
import org.payLate.requestModels.UserOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final UserOrderRepository userOrderRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public OrderService(UserOrderRepository userOrderRepository, JavaMailSender mailSender) {
        this.userOrderRepository = userOrderRepository;
        this.mailSender = mailSender;
    }

    public UserOrder getOrderById(Long id) throws Exception {
        return userOrderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order with ID " + id + " not found"));
    }

    public UserOrder saveUserOrder(UserOrderRequest request) throws Exception {
        if (request == null || request.getUserEmail() == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new Exception("Invalid order request: missing email or items");
        }
        UserOrder order = new UserOrder();
        order.setUserEmail(request.getUserEmail());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = request.getItems().stream().map(itemReq -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(itemReq.getPrice());
            item.setImgUrl(itemReq.getImgUrl());
            item.setPartner(itemReq.getPartner());
            item.setUserOrder(order); // Link back to order
            return item;
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        return userOrderRepository.save(order);
    }


    public List<UserOrder> getUserOrders(String userEmail) {
        return userOrderRepository.findByUserEmail(userEmail);
    }

    public List<UserOrder> getPendingOrders() {
        return userOrderRepository.findByStatus("PENDING");
    }

    public void markOrderAsPaid(Long orderId, String userEmail) throws Exception {
        UserOrder order = getOrderById(orderId);

        order.setStatus("PAID");
        userOrderRepository.save(order);

        sendOrderCompletionEmail(userEmail, order);
    }

    private void sendOrderCompletionEmail(String userEmail, UserOrder order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Order Completion Notification");
        message.setText("Your order with ID " + order.getId() +
                " has been successfully completed and is on its way. It will be delivered within 7 days. Thank you for shopping with us!");
        mailSender.send(message);
    }
}