package br.com.labs;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventListener {

    @RabbitListener(queues = "purchase.eletronics")
    public void onOrderPaidBasic(Mensagem mensagem) {
        System.out.println("Venda de cliente basic paga recebida de id: " + mensagem.getConteudo());
    }
}