package br.com.labs;

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
        Message message = new Message("mensagem para server 8".getBytes());
        rabbitTemplate.convertAndSend(RabbitMQConfig.FINANCE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_PAYMENT_FINANCE_ELETRONICS, message);
    }
    
    @GetMapping
    public String orders() {
        Integer count = (Integer) amqpAdmin.getQueueProperties(RabbitMQConfig.FINANCE_ELETRONICS_QUEUE_NAME).get("QUEUE_MESSAGE_COUNT");
        return String.valueOf(count)+"\n";
    }
    
    @PostMapping("/pays")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pays() throws InterruptedException {
        for(long i = 0; i < 1000000L; i++) {
            String str = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ --------------------------------------------------------------------------- mensagem para server 8 "+String.valueOf(i);
            Message message = new Message(str.getBytes());
            rabbitTemplate.convertAndSend(RabbitMQConfig.FINANCE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_PAYMENT_FINANCE_ELETRONICS, message);
            if (i%1000==0) {
                Thread.sleep(100);
            }
        }
    }
    
}
