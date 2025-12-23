package com.dz4.orders.service;

import com.dz4.orders.dto.OrderDto;
import com.dz4.orders.events.PaymentRequested;
import com.dz4.orders.repo.OrdersRepository;
import com.dz4.orders.repo.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdersService {
  private final OrdersRepository ordersRepo;
  private final OutboxRepository outboxRepo;
  private final ObjectMapper om;

  public OrdersService(OrdersRepository ordersRepo, OutboxRepository outboxRepo, ObjectMapper om) {
    this.ordersRepo = ordersRepo;
    this.outboxRepo = outboxRepo;
    this.om = om;
  }

  @Transactional
  public OrderDto createOrder(String userId, long amount, String description) {
    UUID orderId = UUID.randomUUID();
    UUID eventId = UUID.randomUUID();

    OrderDto created = ordersRepo.insert(orderId, userId, amount, description);

    PaymentRequested ev = new PaymentRequested();
    ev.setEventId(eventId);
    ev.setOrderId(orderId);
    ev.setUserId(userId);
    ev.setAmount(amount);
    ev.setCreatedAt(OffsetDateTime.now());

    try {
      outboxRepo.insert(eventId, "PaymentRequested", orderId, om.writeValueAsString(ev));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("serialize event", e);
    }

    return created;
  }

  public List<OrderDto> listOrders(String userId, int limit) {
    int lim = Math.max(1, Math.min(limit, 200));
    return ordersRepo.listByUser(userId, lim);
  }

  public Optional<OrderDto> getOrder(String userId, UUID id) {
    return ordersRepo.getById(userId, id);
  }

  public void applyPaymentResult(UUID orderId, String status) {
    String newStatus = "CANCELLED";
    if ("SUCCESS".equalsIgnoreCase(status)) {
      newStatus = "FINISHED";
    }
    ordersRepo.applyPaymentResult(orderId, newStatus);
  }
}
