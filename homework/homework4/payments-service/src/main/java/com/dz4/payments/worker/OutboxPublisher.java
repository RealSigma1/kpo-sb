package com.dz4.payments.worker;

import com.dz4.payments.repo.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {
  private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

  private final OutboxRepository repo;
  private final RabbitTemplate rabbit;
  private final String queue;
  private final int batchSize;
  private final int lockTimeoutSec;

  public OutboxPublisher(
      OutboxRepository repo,
      RabbitTemplate rabbit,
      @Value("${app.queues.paymentResult}") String queue,
      @Value("${app.outbox.batchSize}") int batchSize,
      @Value("${app.outbox.lockTimeoutSec}") int lockTimeoutSec
  ) {
    this.repo = repo;
    this.rabbit = rabbit;
    this.queue = queue;
    this.batchSize = batchSize;
    this.lockTimeoutSec = lockTimeoutSec;
  }

  @Scheduled(fixedDelayString = "${app.outbox.publishIntervalMs}")
  public void tick() {
    List<OutboxRepository.OutboxItem> items = repo.lockBatch(batchSize, lockTimeoutSec);
    for (OutboxRepository.OutboxItem it : items) {
      try {
        rabbit.convertAndSend("", queue, it.payloadJson());
        repo.markSent(it.id());
      } catch (Exception e) {
        log.warn("publish failed for outbox id={}: {}", it.id(), e.getMessage());
      }
    }
  }
}
