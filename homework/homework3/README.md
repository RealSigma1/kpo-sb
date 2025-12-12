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

Создаем три файла

```bash
PS C:\Users\Admin> echo "Okay" > test1.txt
PS C:\Users\Admin> echo "Okay" > test2.txt
PS C:\Users\Admin> echo "Okay" > test3.txt
```

Отправляем три запроса

```bash
PS C:\Users\Admin> curl.exe POST http://localhost:8080/works -F "studentName=Inal" -F "assignmentName=HW3" -F "file=@test1.txt"
{"id":1,"studentName":"Inal","assignmentName":"HW3","submittedAt":"2025-12-12T20:38:00.866282231","filePath":"/uploads/99ed9734-9ab1-415a-b298-08c4c134a7ea_test1.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}

PS C:\Users\Admin> curl.exe POST http://localhost:8080/works -F "studentName=Stud2" -F "assignmentName=HW3" -F "file=@test2.txt"
{"id":2,"studentName":"Stud2","assignmentName":"HW3","submittedAt":"2025-12-12T20:38:24.714087939","filePath":"/uploads/b553ee31-571a-48d9-b5ce-e1e54557b104_test2.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}

PS C:\Users\Admin> curl.exe POST http://localhost:8080/works -F "studentName=Stud3" -F "assignmentName=HW3" -F "file=@test3.txt"
{"id":3,"studentName":"Stud3","assignmentName":"HW3","submittedAt":"2025-12-12T20:38:40.500717238","filePath":"/uploads/485c857f-8d8f-42bf-9715-b404a57fdcdf_test3.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}
```

Смотрим отчеты по трем файлам, заметим, что первый отправленный файл имеет статуc "plagiarismDetected":false", в то время как другие два имеют статус "plagiarismDetected:true".

```bash
PS C:\Users\Admin> curl.exe http://localhost:8080/works/1/reports
[{"id":1,"workId":1,"plagiarismDetected":false,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&minWordLength=2","createdAt":"2025-12-12T20:38:01.860557"}]

PS C:\Users\Admin> curl.exe http://localhost:8080/works/2/reports
[{"id":2,"workId":2,"plagiarismDetected":true,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&minWordLength=2","createdAt":"2025-12-12T20:38:24.769593"}]

PS C:\Users\Admin> curl.exe http://localhost:8080/works/3/reports
[{"id":3,"workId":3,"plagiarismDetected":true,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&minWordLength=2","createdAt":"2025-12-12T20:38:40.524341"}]
```


## Логика плагиата

Файл считается плагиатом, если его SHA-256 уже встречался у другой работы.

## Хранилище данных

Используется H2 (Spring Data JPA).



