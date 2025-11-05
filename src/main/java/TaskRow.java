
/**
 * Simple DTO used by MyBatis to map rows from the tasks table.
 */
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskRow implements Serializable{

    private final static long serialVersionUID = 1L;

    private String title;

    private LocalDateTime remindAt;

    private String email;
}