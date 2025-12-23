package com.dz4.orders.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OrderDto {
  private UUID id;
  private String userId;
  private long amount;
  private String description;
  private String status;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public long getAmount() { return amount; }
  public void setAmount(long amount) { this.amount = amount; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
