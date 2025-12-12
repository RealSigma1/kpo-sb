# Homework 3 — Anti-Plagiarism System 

Cистема приёма работ, хранения файлов и анализа на плагиат (по совпадению SHA-256), с выдачей отчётов и ссылкой на облако слов.

## Архитектура

Состоит из 3 сервисов:

- **api-gateway** (порт `8080`) — единая точка входа, маршрутизация запросов
- **file-storing-service** (порт `8081`) — приём работ, сохранение файла, хранение метаданных, триггер анализа
- **file-analysis-service** (порт `8082`) — анализ файла, сохранение отчёта, генерация wordcloud URL

Общий том:
- `./uploads` (на хосте) монтируется в `/uploads` в `file-storing-service` и `file-analysis-service` для обмена файлами.

## Порты и маршруты

### API Gateway (порт 8080)

- `POST /works` → file-storing-service
- `GET /works/{id}` → file-storing-service
- `GET /works/{workId}/reports` → file-analysis-service

Клиент работает только с `http://localhost:8080`.

## Запуск

```bash
docker compose up --build
```

## Пример запроса

```bash
curl -X POST http://localhost:8080/works \
  -F "studentName=Ivan Ivanov" \
  -F "assignmentName=hw3" \
  -F "file=@test.txt"
```

## Логика плагиата

Файл считается плагиатом, если его SHA-256 уже встречался у другой работы.

## Хранилище данных

Используется H2 (Spring Data JPA).



