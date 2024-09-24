package task1;

import java.util.*;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 10:51
 */
public class TaskScheduler {
    PriorityQueue<Task> taskQueue;

    public TaskScheduler() {
        taskQueue = new PriorityQueue<>(
                Comparator.comparing(Task::getPriority).reversed()
                        .thenComparing(Task::getDescription)
        );
    }

    public void addTask(String description, int priority) {
        taskQueue.offer(new Task(description, priority));
    }

    public List<Task> getTopKTasks(int k) {
        List<Task> topTasks = new ArrayList<>();
        PriorityQueue<Task> tempQueue = new PriorityQueue<>(taskQueue);

        while (!tempQueue.isEmpty() && k > 0) {
            topTasks.add(tempQueue.poll());
            k--;
        }

        return topTasks;
    }

    public Task finishNextTask() {
        return taskQueue.poll();
    }
}
