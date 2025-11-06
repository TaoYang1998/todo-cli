package com.ty.todo.task.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis mapper that encapsulates SQL for the tasks table.
 */
@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO tasks(title, remind_at, email) VALUES(#{title}, #{remindAt}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TaskEntity entity);

    @Select("SELECT id, title, remind_at, email FROM tasks ORDER BY id ASC")
    List<TaskEntity> findAll();
}
