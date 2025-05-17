package controller;

import model.Task;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    void removeTask(String title) throws Exception;
    void markTaskAsCompleted(String title) throws Exception;
    List<Task> getAllTasks();
}
