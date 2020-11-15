package pl.edu.pb.todo;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {

    private static TaskStorage taskStorage = new TaskStorage();
    private List<Task> tasks;

    private TaskStorage() {
        tasks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setName("Zadanie #" + i);
            task.setDone(i % 3 == 0);
            tasks.add(task);
        }
    }

    public static TaskStorage getInstance() {
        if (taskStorage == null)
        {
            taskStorage = new TaskStorage();
        }
            return taskStorage;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

}
