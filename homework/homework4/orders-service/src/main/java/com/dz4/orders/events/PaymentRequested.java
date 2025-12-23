package com.dz4.orders.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PaymentRequested {
  private UUID eventId;
  private UUID orderId;
  private String userId;
  private long amount;
  private OffsetDateTime createdAt;

  public UUID getEventId() { return eventId; }
  public void setEventId(UUID eventId) { this.eventId = eventId; }
  public UUID getOrderId() { return orderId; }
  public void setOrderId(UUID orderId) { this.orderId = orderId; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public long getAmount() { return amount; }
  public void setAmount(long amount) { this.amount = amount; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
