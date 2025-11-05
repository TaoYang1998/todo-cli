import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * MyBatis-backed repository implementation.
 */
public final class MyBatisTaskRepository implements TaskRepository {
    private final SqlSessionFactory sessionFactory;
    private final PooledDataSource dataSource;

    public MyBatisTaskRepository(String url, String user, String pass) {
        this.dataSource = new PooledDataSource("com.mysql.cj.jdbc.Driver", url, user, pass);
        var environment = new Environment("todo-cli", new JdbcTransactionFactory(), dataSource);
        var configuration = new Configuration(environment);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.addMapper(TaskMapper.class);
        this.sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Override
    public void add(Task task) {
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TaskMapper.class);
            Timestamp ts = task.remindAt().map(Timestamp::valueOf).orElse(null);
            String email = task.email().orElse(null);
            mapper.insert(task.title(), ts, email);
        }
    }

    @Override
    public List<Task> list() {
        try (SqlSession session = sessionFactory.openSession()) {
            var mapper = session.getMapper(TaskMapper.class);
            var rows = mapper.selectAll();
            var result = new ArrayList<Task>(rows.size());
            for (TaskRow row : rows) {
                Optional<LocalDateTime> remind = Optional.ofNullable(row.getRemindAt());
                Optional<String> email = Optional.ofNullable(row.getEmail());
                result.add(new Task(row.getTitle(), remind, email));
            }
            return result;
        }
    }
}