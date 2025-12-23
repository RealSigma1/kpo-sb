package com.dz4.payments.service;

import com.dz4.payments.events.PaymentRequested;
import com.dz4.payments.events.PaymentResult;
import com.dz4.payments.repo.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentProcessor {
  private final JdbcTemplate jdbc;
  private final OutboxRepository outbox;
  private final ObjectMapper om;

  public PaymentProcessor(JdbcTemplate jdbc, OutboxRepository outbox, ObjectMapper om) {
    this.jdbc = jdbc;
    this.outbox = outbox;
    this.om = om;
  }

  @Transactional
  public void process(PaymentRequested req) {
    try {
      String payload = om.writeValueAsString(req);
      jdbc.update("INSERT INTO inbox(message_id, payload) VALUES(?, ?::jsonb)", req.getEventId(), payload);
    } catch (DataIntegrityViolationException dup) {
      return;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    jdbc.update(
        "INSERT INTO payment_transactions(order_id, user_id, amount, status, reason) " +
            "VALUES(?, ?, ?, 'PROCESSING', '') ON CONFLICT (order_id) DO NOTHING",
        req.getOrderId(), req.getUserId(), req.getAmount()
    );

    List<TxRow> txRows = jdbc.query(
        "SELECT status, reason FROM payment_transactions WHERE order_id=? FOR UPDATE",
        (rs, rn) -> new TxRow(rs.getString("status"), rs.getString("reason")),
        req.getOrderId()
    );
    if (txRows.isEmpty()) {
      throw new IllegalStateException("payment_transactions row missing");
    }
    TxRow tx = txRows.get(0);
    if (!"PROCESSING".equals(tx.status)) {
      ensureOutboxResult(req.getOrderId(), tx.status, tx.reason);
      return;
    }

    String status;
    String reason;

    Long bal = null;
    try {
      bal = jdbc.queryForObject("SELECT balance FROM accounts WHERE user_id=?", Long.class, req.getUserId());
    } catch (Exception e) {
      bal = null;
    }

    if (bal == null) {
      status = "FAIL";
      reason = "NO_ACCOUNT";
    } else {
      int updated = jdbc.update(
          "UPDATE accounts SET balance=balance-?, updated_at=now() WHERE user_id=? AND balance >= ?",
          req.getAmount(), req.getUserId(), req.getAmount()
      );
      if (updated == 1) {
        status = "SUCCESS";
        reason = "";
      } else {
        status = "FAIL";
        reason = "INSUFFICIENT_FUNDS";
      }
    }

    jdbc.update(
        "UPDATE payment_transactions SET status=?, reason=?, updated_at=now() WHERE order_id=?",
        status, reason, req.getOrderId()
    );

    ensureOutboxResult(req.getOrderId(), status, reason);
  }

  private void ensureOutboxResult(UUID orderId, String status, String reason) {
    PaymentResult res = new PaymentResult();
    res.setEventId(UUID.randomUUID());
    res.setOrderId(orderId);
    res.setStatus(status);
    res.setReason(reason);
    res.setCreatedAt(OffsetDateTime.now());

    try {
      outbox.insert(res.getEventId(), "PaymentResult", orderId, om.writeValueAsString(res));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private static class TxRow {
    final String status;
    final String reason;
    TxRow(String status, String reason) {
      this.status = status;
      this.reason = reason;
    }
  }
}
