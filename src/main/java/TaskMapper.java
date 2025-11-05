import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis mapper that handles CRUD operations for tasks.
 */
public interface TaskMapper {
    @Insert("INSERT todo_task INTO tasks(title, remind_at, email) VALUES(#{title}, #{remindAt}, #{email})")
    void insert(@Param("title") String title,
                @Param("remindAt") java.sql.Timestamp remindAt,
                @Param("email") String email);

    @Select("SELECT title, remind_at, email FROM todo_task ORDER BY id ASC")
    @Results({
        @Result(property = "title", column = "title"),
        @Result(property = "remindAt", column = "remind_at"),
        @Result(property = "email", column = "email")
    })
    List<TaskRow> selectAll();

}