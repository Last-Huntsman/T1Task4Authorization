# T1Task4 — Spring Boot JWT Аутентификация

## 📌 Описание

Это backend-приложение на основе Spring Boot, реализующее:

- JWT-аутентификацию (доступ + refresh токены)
- Регистрацию и вход пользователя
- Ротацию и отзыв refresh токенов
- Ролевую модель (`ROLE_ADMIN`, `ROLE_PREMIUM_USER`, `ROLE_GUEST`)
- Хранение пользователей и отозванных токенов в PostgreSQL
- Защищённые эндпоинты
- Docker-сборку

---

## 🚀 Технологии

- Java 21
- Spring Boot 3.5.3
- Spring Security
- JPA (Hibernate)
- PostgreSQL
- JWT (JJWT)
- Docker

---

## ⚙️ Установка и запуск

### 🔧 Локально (без Docker)

1. Убедитесь, что установлен PostgreSQL и создана БД:

```bash
CREATE DATABASE t1task4db;
```

2. Проверьте настройки в `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/t1task4db
spring.datasource.username=postgres
spring.datasource.password=1234
jwt.secret=NynfbhrFo+xlrSwcdDtkfu8xZhmELRclNVv0lwm451U=
```

3. Запустите приложение:

```bash
mvn clean spring-boot:run
```

---

### 🐳 Через Docker

```bash
docker-compose up --build
```

- Приложение будет доступно на `http://localhost:8080`
- База данных PostgreSQL будет работать на `localhost:5432`

---

## 📬 API Эндпоинты

### 🔐 Аутентификация

- `POST /auth/register` — регистрация нового пользователя  
- `POST /auth/login` — вход по email/паролю  
- `POST /auth/refresh` — обновление токена  
- `POST /auth/logout` — отзыв токена  
- `GET /auth/me/{email}` — получить данные о себе (нужен `Authorization: Bearer <token>`)

---

## 📦 Пример запроса/ответа

### 🔑 Login:

```json
POST /auth/login
{
  "email": "test@example.com",
  "password": "1234"
}
```

**Ответ:**
```json
{
  "token": "<ACCESS_TOKEN>",
  "refreshToken": "<REFRESH_TOKEN>"
}
```

---

## 🛡 Безопасность

- Доступ к `/auth/**` — публичный
- Все остальные эндпоинты требуют JWT токен в `Authorization` header
- Все токены проверяются на отзыв (RevokedToken)

---

## 🧪 Тестирование

```bash
mvn test
```

---

## 📄 License

MIT License
---

## 📚 Примеры всех доступных запросов

### ✅ Регистрация пользователя

**POST** `/auth/register`  
**Request Body:**
```json
{
  "firstName": "Ivan",
  "lastName": "Ivanov",
  "email": "ivan@example.com",
  "password": "123456"
}
```
**Response:**
```
User registered with ID: <UUID>
```

---

### 🔑 Вход пользователя

**POST** `/auth/login`  
**Request Body:**
```json
{
  "email": "ivan@example.com",
  "password": "123456"
}
```
**Response:**
```json
{
  "token": "<ACCESS_TOKEN>",
  "refreshToken": "<REFRESH_TOKEN>"
}
```

---

### ♻️ Обновление токена

**POST** `/auth/refresh`  
**Request Body:**
```json
{
  "refreshToken": "<REFRESH_TOKEN>"
}
```
**Response:**
```json
{
  "token": "<NEW_ACCESS_TOKEN>",
  "refreshToken": "<NEW_REFRESH_TOKEN>"
}
```

---

### 🔐 Получение информации о пользователе

**GET** `/auth/me/{email}`  
**Headers:**
```
Authorization: Bearer <ACCESS_TOKEN>
```
**Response:**
```json
{
  "userId": "<UUID>",
  "firstName": "Ivan",
  "lastName": "Ivanov",
  "email": "ivan@example.com",
  "password": "$2a$12$..."
}
```

---

### 🚪 Выход пользователя (отзыв токена)

**POST** `/auth/logout`  
**Request Body:**
```json
{
  "token": "<ANY_TOKEN>"
}
```
**Response:**
```
Token has been revoked
```
