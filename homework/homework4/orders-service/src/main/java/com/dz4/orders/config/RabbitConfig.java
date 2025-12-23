package com.dz4.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Bean
  public Queue paymentRequestQueue(@Value("${app.queues.paymentRequest}") String name) {
    return new Queue(name, true);
  }

  @Bean
  public Queue paymentResultQueue(@Value("${app.queues.paymentResult}") String name) {
    return new Queue(name, true);
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
    return new RabbitAdmin(cf);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());
    return om;
  }

  @Bean
  public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper om) {
    return new Jackson2JsonMessageConverter(om);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
    RabbitTemplate rt = new RabbitTemplate(cf);
    rt.setMessageConverter(conv);
    return rt;
  }
}
