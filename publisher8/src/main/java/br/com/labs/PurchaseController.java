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
@RequestMapping(value = "/v1/purchase")
public class PurchaseController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void buy() {
        Message message = new Message("mensagem para server 8".getBytes());
        rabbitTemplate.convertAndSend(RabbitMQConfig.PURCHASE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_PAYMENT_PURCHASE_ELETRONICS, message);
    }
    
    @GetMapping
    public String sales() {
        Integer count = (Integer) amqpAdmin.getQueueProperties(RabbitMQConfig.PURCHASE_ELETRONICS_QUEUE_NAME).get("QUEUE_MESSAGE_COUNT");
        return String.valueOf(count)+"\n";
    }
    
    @PostMapping("/buys")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void buys() throws InterruptedException {
        for(int i = 0; i < 10000000; i++) {
            String str = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ --------------------------------------------------------------------------- mensagem para server 8 "+String.valueOf(i);
            Message message = new Message(str.getBytes());
            rabbitTemplate.convertAndSend(RabbitMQConfig.PURCHASE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_PAYMENT_PURCHASE_ELETRONICS, message);
            if (i%2000==0) {
                Thread.sleep(100);
            }
        }
    }
    
}
