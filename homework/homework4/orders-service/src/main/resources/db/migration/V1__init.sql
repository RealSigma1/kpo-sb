CREATE TABLE IF NOT EXISTS orders (
  id UUID PRIMARY KEY,
  user_id TEXT NOT NULL,
  amount BIGINT NOT NULL,
  description TEXT NOT NULL,
  status TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS orders_user_created_idx ON orders(user_id, created_at DESC);

CREATE TABLE IF NOT EXISTS outbox (
  id UUID PRIMARY KEY,
  event_type TEXT NOT NULL,
  aggregate_id UUID NOT NULL,
  payload JSONB NOT NULL,
  status TEXT NOT NULL,
  attempts INT NOT NULL DEFAULT 0,
  locked_at TIMESTAMPTZ NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  sent_at TIMESTAMPTZ NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS outbox_unique_event_idx ON outbox(event_type, aggregate_id);
CREATE INDEX IF NOT EXISTS outbox_status_created_idx ON outbox(status, created_at);
