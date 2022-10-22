package br.com.labs;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/finance")
public class FinanceController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pay() {
        Message message = new Message("mensagem para server 3".getBytes());
        rabbitTemplate.convertAndSend(RabbitMQConfig.FINANCE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_PAYMENT_FINANCE_ELETRONICS, message);
    }
    
    @GetMapping
    public String orders() {
        Integer count = (Integer) amqpAdmin.getQueueProperties(RabbitMQConfig.FINANCE_ELETRONICS_QUEUE_NAME).get("QUEUE_MESSAGE_COUNT");
        return String.valueOf(count)+"\n";
    }
    
    @GetMapping("/content")
    public String content() {
        Message message = rabbitTemplate.receive(RabbitMQConfig.FINANCE_ELETRONICS_QUEUE_NAME);
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        return s;
    }
    
}
