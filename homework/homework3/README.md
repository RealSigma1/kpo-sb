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
PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe -X POST "http://localhost:8080/works" -F "studentName=Inal" -F "assignmentName=HW3" -F "file=@test1.txt"
{"id":1,"studentName":"Inal","assignmentName":"HW3","submittedAt":"2025-12-12T20:52:46.274529957","filePath":"/uploads/e30a0fdc-1027-4255-b83e-593d6d3846a5_test1.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}

PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe -X POST "http://localhost:8080/works" -F "studentName=Stud2" -F "assignmentName=HW3" -F "file=@test2.txt"
{"id":2,"studentName":"Stud2","assignmentName":"HW3","submittedAt":"2025-12-12T20:52:59.286102287","filePath":"/uploads/289c0e23-e015-44a9-aeae-0474ec5ed448_test2.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}

PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe -X POST "http://localhost:8080/works" -F "studentName=Stud3" -F "assignmentName=HW3" -F "file=@test3.txt"
{"id":3,"studentName":"Stud3","assignmentName":"HW3","submittedAt":"2025-12-12T20:53:12.321796739","filePath":"/uploads/56e3d7d9-a13a-4e69-9e01-50e2d7c79a4c_test3.txt","fileHash":"a72cd2b01ba06110918793f4549f7cd14584bdaf13939919f9e77b6bc80680ec"}
```

Смотрим отчеты по трем файлам, заметим, что первый отправленный файл имеет статуc "plagiarismDetected":false", в то время как другие два имеют статус "plagiarismDetected:true".

```bash
PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe http://localhost:8080/works/1/reports
[{"id":1,"workId":1,"plagiarismDetected":false,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&backgroundColor=white","createdAt":"2025-12-12T20:52:47.450958"}]

PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe http://localhost:8080/works/2/reports
[{"id":2,"workId":2,"plagiarismDetected":true,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&backgroundColor=white","createdAt":"2025-12-12T20:52:59.310316"}]

PS C:\Users\Admin\desktop\kpo-Shidakov\homework\homework3> curl.exe http://localhost:8080/works/3/reports
[{"id":3,"workId":3,"plagiarismDetected":true,"wordCloudUrl":"https://quickchart.io/wordcloud?text=o+k+a+y&format=png&width=800&height=400&fontFamily=Arial&fontScale=60&scale=linear&removeStopwords=false&backgroundColor=white","createdAt":"2025-12-12T20:53:12.33911"}]
```
\
Следовательно, облако слов на основе данных запросов выглядит так:
<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/d998df22-8eb3-464b-b772-ec7b8d50a8d7" />


## Логика плагиата

Файл считается плагиатом, если его SHA-256 уже встречался у другой работы.

## Хранилище данных

Используется H2 (Spring Data JPA).



