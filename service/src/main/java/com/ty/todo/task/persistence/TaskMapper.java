package com.ty.todo.task.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * MyBatis mapper that encapsulates SQL for the tasks table.
 */
@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO tasks(title, remind_at, email, reminded_at) VALUES(#{title}, #{remindAt}, #{email}, #{remindedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TaskEntity entity);

    @Select("SELECT id, title, remind_at, email, reminded_at FROM tasks ORDER BY id ASC")
    List<TaskEntity> findAll();

    @Select("SELECT id, title, remind_at, email, reminded_at FROM tasks WHERE email IS NOT NULL AND remind_at IS NOT NULL AND remind_at <= #{threshold} AND reminded_at IS NULL")
    List<TaskEntity> findPendingReminders(@Param("threshold") LocalDateTime threshold);

    @Update({
            "<script>",
            "UPDATE tasks",
            "SET reminded_at = #{when}",
            "WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int markReminded(@Param("ids") List<Long> ids, @Param("when") LocalDateTime when);
}
