package com.dz4.orders.http;

import com.dz4.orders.dto.CreateOrderRequest;
import com.dz4.orders.dto.OrderDto;
import com.dz4.orders.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrdersController {
  private final OrdersService svc;

  public OrdersController(OrdersService svc) {
    this.svc = svc;
  }

  @GetMapping("/health")
  public Map<String, String> health() {
    return Map.of("status", "ok");
  }

  @PostMapping("/orders")
  public ResponseEntity<?> create(
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @Valid @RequestBody CreateOrderRequest req
  ) {
    OrderDto created = svc.createOrder(userId, req.getAmount(), req.getDescription());
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/orders")
  public List<OrderDto> list(
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestParam(value = "limit", required = false, defaultValue = "100") int limit
  ) {
    return svc.listOrders(userId, limit);
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<?> get(
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @PathVariable("id") UUID id
  ) {
    return svc.getOrder(userId, id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "order not found")));
  }
}
