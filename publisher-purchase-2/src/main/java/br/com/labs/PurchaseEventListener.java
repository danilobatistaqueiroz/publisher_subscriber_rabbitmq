package br.com.labs;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventListener {
    @RabbitListener(queues = RabbitMQConfig.PURCHASE_ELETRONICS_QUEUE_NAME)
    public void onPurchaseEvent(Message message) {
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("Recebido: "+s);
    }
}
