package com.dz4.payments.dto;

import jakarta.validation.constraints.Min;

public class TopUpRequest {
  @Min(1)
  private long amount;

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }
}
