package com.dz4.orders.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class OutboxRepository {
  private final JdbcTemplate jdbc;

  public OutboxRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public void insert(UUID id, String eventType, UUID aggregateId, String payloadJson) {
    jdbc.update(
            "INSERT INTO outbox(id, event_type, aggregate_id, payload, status) VALUES(?,?,?, ?::jsonb, 'NEW') " +
                    "ON CONFLICT (event_type, aggregate_id) DO NOTHING",
            id, eventType, aggregateId, payloadJson
    );

  }

  public record OutboxItem(UUID id, String payloadJson) {}

  private static final RowMapper<OutboxItem> MAPPER = new RowMapper<>() {
    @Override
    public OutboxItem mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new OutboxItem(UUID.fromString(rs.getString("id")), rs.getString("payload"));
    }
  };

  @Transactional
  public List<OutboxItem> lockBatch(int batch, int lockTimeoutSec) {
    String q =
        "SELECT id, payload::text AS payload " +
        "FROM outbox " +
        "WHERE status IN ('NEW','IN_FLIGHT') " +
        "  AND (locked_at IS NULL OR locked_at < now() - (? || ' seconds')::interval) " +
        "ORDER BY created_at " +
        "FOR UPDATE SKIP LOCKED " +
        "LIMIT ?";

    List<OutboxItem> items = jdbc.query(q, MAPPER, lockTimeoutSec, batch);
    for (OutboxItem it : items) {
      jdbc.update("UPDATE outbox SET status='IN_FLIGHT', locked_at=now(), attempts=attempts+1 WHERE id=?", it.id());
    }
    return new ArrayList<>(items);
  }

  public void markSent(UUID id) {
    jdbc.update("UPDATE outbox SET status='SENT', sent_at=now() WHERE id=?", id);
  }
}
