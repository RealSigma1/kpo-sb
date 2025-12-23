package com.dz4.orders.worker;

import com.dz4.orders.events.PaymentResult;
import com.dz4.orders.service.OrdersService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentResultListener {
    private static final Logger log = LoggerFactory.getLogger(PaymentResultListener.class);

    private final OrdersService svc;
    private final ObjectMapper mapper;

    public PaymentResultListener(OrdersService svc, ObjectMapper mapper) {
        this.svc = svc;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${app.queues.paymentResult}")
    public void onMessage(String payload, Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            PaymentResult ev = parsePaymentResult(payload);
            svc.applyPaymentResult(ev.getOrderId(), ev.getStatus());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.warn("failed to apply payment result: {}", e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }

    private PaymentResult parsePaymentResult(String payload) throws Exception {
        JsonNode node = mapper.readTree(payload);
        if (node.isTextual()) {
            node = mapper.readTree(node.asText());
        }
        return mapper.treeToValue(node, PaymentResult.class);
    }
}
