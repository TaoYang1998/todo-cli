# Project: todo-service
Author: taoyang
JDK Version: 25
Language: Java

---

## 🎯 Goal
将原有的命令行工具演进为 Spring Boot REST 服务，提供待办事项的创建与查询接口，并保留使用 MyBatis/MySQL 的持久化能力（默认提供 H2 内存数据库便于启动）。

---

## 🧩 Technical Overview

- **Framework:** Spring Boot 3.3 + Spring MVC
- **Persistence:** MyBatis（TaskMapper 注解版）
- **Database:** MySQL（生产）；H2（默认开发/测试，自动加载 `schema.sql`）
- **Build Tool:** Maven
- **Java Version:** 25（可兼容 17+）

---

## 📡 REST Endpoints

| Method | Path         | Description        |
|--------|--------------|--------------------|
| POST   | `/api/tasks` | 新建任务，返回 201 |
| GET    | `/api/tasks` | 列出所有任务       |

### Request JSON (`POST /api/tasks`)
```json
{
  "title": "买牛奶",
  "remindAt": "2025-10-28T09:00:00", 
  "email": "me@example.com"
}
```

### Response JSON
```json
{
  "id": 1,
  "title": "买牛奶",
  "remindAt": "2025-10-28T09:00:00",
  "email": "me@example.com"
}
```

- 若 `remindAt` 早于当前时间，会返回 400。

---

## ⚙️ Configuration

| Environment Variable | Default (H2)                                        |
|----------------------|-----------------------------------------------------|
| `TODO_DB_URL`        | `jdbc:h2:mem:todo;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` |
| `TODO_DB_USER`       | `sa`                                                                                         |
| `TODO_DB_PASS`       | ``                                                                                          |
| `TODO_SERVER_PORT`   | `8080`                                                                                      |

- 生产环境只需提供 MySQL JDBC URL 与凭证。
- 表结构定义见 `src/main/resources/schema.sql`。

---

## 🚀 Run
```bash
mvn spring-boot:run
```
或打包后运行：
```bash
mvn package
java -jar target/todo-cli-1.0.0-SNAPSHOT.jar
```

---

## 🧪 Tests
- `mvn test`（使用 Spring Boot starter test + H2）

---

## 📝 Notes
- 处理逻辑集中在 `TaskService`，输入通过 `Task` 领域模型标准化。
- `RestExceptionHandler` 将 `IllegalArgumentException` 转换成 400 响应。
- MyBatis Mapper 位于 `com.ty.todo.task.persistence`，支持自动生成主键。