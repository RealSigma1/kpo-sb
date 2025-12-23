package com.dz4.payments.worker;

import com.dz4.payments.events.PaymentRequested;
import com.dz4.payments.service.PaymentProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestedListener {
    private static final Logger log = LoggerFactory.getLogger(PaymentRequestedListener.class);

    private final PaymentProcessor processor;
    private final ObjectMapper mapper;

    public PaymentRequestedListener(PaymentProcessor processor, ObjectMapper mapper) {
        this.processor = processor;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${app.queues.paymentRequest}")
    public void onMessage(String payload, Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            PaymentRequested req = parsePaymentRequested(payload);
            processor.process(req);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.warn("process failed: {}", e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }

    private PaymentRequested parsePaymentRequested(String payload) throws Exception {
        JsonNode node = mapper.readTree(payload);
        if (node.isTextual()) {
            node = mapper.readTree(node.asText());
        }
        return mapper.treeToValue(node, PaymentRequested.class);
    }
}
