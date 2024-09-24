package task1;

import lombok.Data;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 10:51
 */
@Data
public class Task {
    String description;
    int priority;

    public Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }
}
