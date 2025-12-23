package com.dz4.orders.repo;

import com.dz4.orders.dto.OrderDto;
import com.dz4.orders.model.OrderStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrdersRepository {
  private final JdbcTemplate jdbc;

  public OrdersRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private static final RowMapper<OrderDto> MAPPER = new RowMapper<>() {
    @Override
    public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
      OrderDto o = new OrderDto();
      o.setId(UUID.fromString(rs.getString("id")));
      o.setUserId(rs.getString("user_id"));
      o.setAmount(rs.getLong("amount"));
      o.setDescription(rs.getString("description"));
      o.setStatus(rs.getString("status"));
      o.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
      o.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
      return o;
    }
  };

  public OrderDto insert(UUID id, String userId, long amount, String description) {
    jdbc.update(
        "INSERT INTO orders(id, user_id, amount, description, status) VALUES(?,?,?,?,?)",
        id, userId, amount, description, OrderStatus.NEW.name()
    );
    return getById(userId, id).orElseThrow();
  }

  public List<OrderDto> listByUser(String userId, int limit) {
    return jdbc.query(
        "SELECT id, user_id, amount, description, status, created_at, updated_at " +
            "FROM orders WHERE user_id=? ORDER BY created_at DESC LIMIT ?",
        MAPPER, userId, limit
    );
  }

  public Optional<OrderDto> getById(String userId, UUID id) {
    List<OrderDto> r = jdbc.query(
        "SELECT id, user_id, amount, description, status, created_at, updated_at " +
            "FROM orders WHERE id=? AND user_id=?",
        MAPPER, id, userId
    );
    if (r.isEmpty()) return Optional.empty();
    return Optional.of(r.get(0));
  }

  public void applyPaymentResult(UUID orderId, String newStatus) {
    jdbc.update(
        "UPDATE orders SET status=?, updated_at=now() WHERE id=? AND status='NEW'",
        newStatus, orderId
    );
  }
}
