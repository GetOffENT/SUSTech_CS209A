package task1;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 10:53
 */
public class TestSchedule {


    @Test
    public void test(){
        TaskScheduler scheduler = new TaskScheduler();
        scheduler.addTask("Write report", 2);
        scheduler.addTask("Respond to emails", 1);
        scheduler.addTask("Prepare presentation", 3);
        scheduler.addTask("Code review", 2);
        scheduler.addTask("Team meeting", 5);
        scheduler.addTask("Project planning", 4);
        scheduler.addTask("Client follow-up", 3);
        scheduler.addTask("Bug fixing", 2);
        scheduler.addTask("Lunch break", 1);
        scheduler.addTask("Team outing", 1);

        System.out.println("Top 5 priority tasks:");
        List<Task> top5Tasks = scheduler.getTopKTasks(5);
        top5Tasks.forEach(System.out::println);
        System.out.println("\nFinishing the next 3 highest priority tasks\n");
        scheduler.finishNextTask();
        scheduler.finishNextTask();
        scheduler.finishNextTask();
        System.out.println("Top 6 priority tasks:");
        List<Task> top6Tasks = scheduler.getTopKTasks(6);
        top6Tasks.forEach(System.out::println);
    }
}
