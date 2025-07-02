package org.payLate.services;

import org.payLate.entity.UserOrder;
import org.payLate.repository.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

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