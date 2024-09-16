package com.products.notificationservice.emailService;//package com.products.customerf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.notificationservice.dtos.PaymentUrlEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics="payment-url-topic", groupId = "email-service")
    public void listen(String message) {

        PaymentUrlEmailDto paymentUrlEmailDto = null;
        try {
            paymentUrlEmailDto = objectMapper.readValue(message, PaymentUrlEmailDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        sendEmail(paymentUrlEmailDto.getEmail(),"Pay for"+paymentUrlEmailDto.getOrderId(),paymentUrlEmailDto.getPaymentUrl());
        System.out.println("Welcome Email has been Sent to "+paymentUrlEmailDto.getEmail());
    }

    private void sendEmail(String toEmail, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}
