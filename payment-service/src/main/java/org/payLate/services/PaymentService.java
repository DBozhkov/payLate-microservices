package org.payLate.services;

import org.payLate.entity.Payment;
import org.payLate.repository.PaymentRepository;
import org.payLate.requestmodels.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebClient stripeWebClient;
    private final WebClient userOrderServiceWebClient;
    private final WebClient cartServiceWebClient;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          WebClient stripeWebClient,
                          WebClient userOrderServiceWebClient,
                          WebClient cartServiceWebClient) {
        this.paymentRepository = paymentRepository;
        this.stripeWebClient = stripeWebClient;
        this.userOrderServiceWebClient = userOrderServiceWebClient;
        this.cartServiceWebClient = cartServiceWebClient;
    }

    public Map<String, Object> createPaymentIntent(PaymentRequest paymentRequest) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentRequest.getAmount());
        params.put("currency", paymentRequest.getCurrency());
        params.put("payment_method_types", new String[]{"card"});
        params.put("receipt_email", paymentRequest.getReceiptEmail());

        return stripeWebClient.post()
                .uri("/v1/payment_intents")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Long createOrder(String userEmail) {
        return userOrderServiceWebClient.post()
                .uri("/api/orders/create")
                .bodyValue(Map.of("userEmail", userEmail))
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void completePayment(String userEmail, Long orderId) throws Exception {
        userOrderServiceWebClient.put()
                .uri("/api/orders/mark-as-paid")
                .bodyValue(Map.of("userEmail", userEmail, "orderId", orderId))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        cartServiceWebClient.delete()
                .uri("/api/cart/clear")
                .header("Authorization", "Bearer " + userEmail)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        Payment payment = paymentRepository.findByUserEmail(userEmail);
        if (payment == null) {
            throw new Exception("Payment information is missing for user: " + userEmail);
        }

        payment.setAmount(0.00);
        payment.setOrderId(orderId);
        paymentRepository.save(payment);
    }
}