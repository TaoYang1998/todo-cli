# Project: todo-cli
Author: taoyang
JDK Version: 25
Language: Java

---

## 🎯 Goal
实现一个命令行待办事项工具（todo-cli），使用 Picocli 框架，支持添加、列出任务，并为将来扩展 SQLite 和邮件提醒做准备。

---

## 🧩 Technical Requirements

1. **Language:** Java 25
2. **CLI Framework:** Picocli
3. **Build Tool:** Gradle
4. **Database:** 暂时用内存 List<Task> 存储，后续再换 SQLite。
5. **Features:**
    - `add`：添加任务，参数有：
        - `--title` 任务标题
        - `--at` 提醒时间（ISO-8601 格式）
        - `--email` 收件邮箱
    - `list`：列出所有任务
6. **Structure:**
    - `Main.java`：入口类，注册子命令
    - `AddCommand.java`：实现 add 命令
    - `ListCommand.java`：实现 list 命令
    - `Task.java`：定义任务数据结构（使用 record）
7. **Requirements:**
    - 所有类应有简短注释
    - 可通过命令行运行：
      ```bash
      java Main add --title "买牛奶" --at "2025-10-28T09:00" --email "me@example.com"
      java Main list
      ```

---

## 📦 Output
请生成以下文件：

- `src/main/java/Main.java`
- `src/main/java/AddCommand.java`
- `src/main/java/ListCommand.java`
- `src/main/java/Task.java`
- `pom.xml` （包含依赖 `picocli`）

---

## 🧠 Notes
- 确保每个文件都包含 import 语句。
- 使用 Java 25 的语法（例如 record、var）。
- 暂不实现数据库，仅使用内存。
- 每个命令的输出可以用 `System.out.println`。