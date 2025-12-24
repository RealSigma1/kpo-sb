# Microservices: Orders + Payments

 Проект из **двух микросервисов** с асинхронной оплатой заказа через RabbitMQ и хранением состояния в Postgres.

Пользователю доступны операции:
- **Payments Service**: создание счёта (не более одного на пользователя), пополнение, просмотр баланса.
- **Orders Service**: создание заказа (асинхронно запускает оплату), список заказов, статус заказа.

---

## Архитектура

- **Orders Service** (REST):
  - принимает заказы;
  - пишет заказ в свою БД;
  - кладёт событие `PaymentRequested` в **outbox** (в рамках той же транзакции);
  - фоновый воркер публикует outbox-сообщения в RabbitMQ;
  - по результату оплаты переводит заказ в `FINISHED` или `CANCELLED`.

- **Payments Service** (REST + RabbitMQ consumer):
  - управляет счётом пользователя (создать/пополнить/баланс);
  - слушает `PaymentRequested` из RabbitMQ;
  - использует **inbox** + `payment_transactions(order_id PK)` чтобы обработать оплату **идемпотентно**;
  - публикует `PaymentProcessed` через outbox в RabbitMQ.

- **RabbitMQ** — транспорт сообщений (at-least-once доставка).
- **Postgres** — отдельная БД на каждый сервис (миграции Flyway).

---

## Сервисы и порты (docker-compose)

- Orders: `localhost:8080`
- Payments: `localhost:8081`
- RabbitMQ AMQP: `localhost:5672`
- RabbitMQ UI: `localhost:15672` (логин/пароль: `guest/guest`)
- Postgres (orders): `localhost:5433`
- Postgres (payments): `localhost:5434`

---

## Запуск

```bash
docker compose up --build
```

Проверка health:
```bash
curl -s http://localhost:8080/health
curl -s http://localhost:8081/health
```

---

## API (основные ручки)

### Payments Service

- `POST /accounts` — создать счёт
- `POST /accounts/topup` — пополнить счёт
- `GET  /accounts/balance` — посмотреть баланс

### Orders Service

- `POST /orders` — создать заказ (асинхронно запускает оплату)
- `GET  /orders` — список заказов
- `GET  /orders/{id}` — статус/детали заказа

---


Статусы:
- `NEW` — заказ создан, оплата в процессе
- `FINISHED` — оплата прошла
- `CANCELLED` — недостаточно средств / отказ в оплате

---

## Пример

```powershell
# 1) account + topup
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/accounts" `
  -Headers @{ "X-User-Id" = "u1" } `
  -ContentType "application/json" `
  -Body (@{} | ConvertTo-Json)

Invoke-RestMethod -Method Post -Uri "http://localhost:8081/accounts/topup" `
  -Headers @{ "X-User-Id" = "u1" } `
  -ContentType "application/json" `
  -Body (@{ amount = 1000 } | ConvertTo-Json)

Invoke-RestMethod -Method Get -Uri "http://localhost:8081/accounts/balance" `
  -Headers @{ "X-User-Id" = "u1" }

# 2) create order
$o = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/orders" `
  -Headers @{ "X-User-Id" = "u1" } `
  -ContentType "application/json" `
  -Body (@{ amount = 100; description = "coffee" } | ConvertTo-Json)

# 3) poll status
1..20 | ForEach-Object {
  $st = Invoke-RestMethod -Method Get -Uri ("http://localhost:8080/orders/{0}" -f $o.id) `
    -Headers @{ "X-User-Id" = "u1" }
  "{0}: {1}" -f $_, $st.status
  if ($st.status -ne "NEW") { break }
  Start-Sleep -Seconds 1
}
```

---

## Фронтенд 

Фронтенд — отдельный контейнер (nginx + статическая страница), который:
- отдаёт UI;
- проксирует запросы на бэкенды по путям:
  - `/api/orders/*` → `orders:8080`
  - `/api/payments/*` → `payments:8081`
