package com.dz4.payments.http;

import com.dz4.payments.dto.TopUpRequest;
import com.dz4.payments.repo.AccountsRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AccountsController {
  private final AccountsRepository repo;

  public AccountsController(AccountsRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/health")
  public Map<String, String> health() {
    return Map.of("status", "ok");
  }

  @PostMapping("/accounts")
  public ResponseEntity<?> create(@RequestHeader(value = "X-User-Id", required = true) String userId) {
    repo.createIfAbsent(userId);
    long bal = repo.getBalance(userId).orElse(0L);
    return ResponseEntity.ok(Map.of("user_id", userId, "balance", bal));
  }

  @PostMapping("/accounts/topup")
  public ResponseEntity<?> topup(
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @Valid @RequestBody TopUpRequest req
  ) {
    boolean ok = repo.topUp(userId, req.getAmount());
    if (!ok) {
      return ResponseEntity.status(404).body(Map.of("error", "account not found"));
    }
    long bal = repo.getBalance(userId).orElse(0L);
    return ResponseEntity.ok(Map.of("user_id", userId, "balance", bal));
  }

  @GetMapping("/accounts/balance")
  public ResponseEntity<?> balance(@RequestHeader(value = "X-User-Id", required = true) String userId) {
    return repo.getBalance(userId)
        .<ResponseEntity<?>>map(b -> ResponseEntity.ok(Map.of("user_id", userId, "balance", b)))
        .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "account not found")));
  }
}
