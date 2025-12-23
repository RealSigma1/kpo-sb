package com.dz4.payments.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountsRepository {
  private final JdbcTemplate jdbc;

  public AccountsRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public void createIfAbsent(String userId) {
    jdbc.update("INSERT INTO accounts(user_id) VALUES(?) ON CONFLICT (user_id) DO NOTHING", userId);
  }

  public Optional<Long> getBalance(String userId) {
    try {
      Long bal = jdbc.queryForObject("SELECT balance FROM accounts WHERE user_id=?", Long.class, userId);
      return Optional.ofNullable(bal);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public boolean topUp(String userId, long amount) {
    int n = jdbc.update("UPDATE accounts SET balance=balance+?, updated_at=now() WHERE user_id=?", amount, userId);
    return n == 1;
  }
}
