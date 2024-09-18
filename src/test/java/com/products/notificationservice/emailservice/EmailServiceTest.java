package com.products.notificationservice.emailservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.notificationservice.dtos.PaymentUrlEmailDto;
import com.products.notificationservice.emailService.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EmailServiceTest {


    @Mock
    JavaMailSender mailSender;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    EmailService emailService;

    private PaymentUrlEmailDto paymentUrlEmailDto;

    @BeforeEach
    public void setUp() {
        paymentUrlEmailDto = new PaymentUrlEmailDto();
        paymentUrlEmailDto.setEmail("email@email.com");
        paymentUrlEmailDto.setAmount(1000);
        paymentUrlEmailDto.setOrderId(1234);
        paymentUrlEmailDto.setPaymentUrl("paymentUrl");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_successfullEmailSend() throws JsonProcessingException {

        when(objectMapper.readValue(anyString(),eq(PaymentUrlEmailDto.class))).thenReturn(paymentUrlEmailDto);
        doNothing().when(mailSender).send(new SimpleMailMessage());

        emailService.listen("{ \"email\": \"testuser@example.com\", \"orderId\": \"12345\", \"paymentUrl\": \"http://payment-link.com\"}");
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void test_failedJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(),eq(PaymentUrlEmailDto.class))).thenThrow(new JsonProcessingException("Error!"){});
        try{
            emailService.listen("Invalid JSON message!");
        }catch(Exception e){
            e.printStackTrace();
        }
        verify(mailSender,never()).send(any(SimpleMailMessage.class));
    }
}
