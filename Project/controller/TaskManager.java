package controller;

import exceptions.TaskNotFoundException;
import model.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    void removeTask(String title) throws TaskNotFoundException;
    void markTaskAsCompleted(String title) throws TaskNotFoundException;
    List<Task> getAllTasks();
    List<Task> getDeletedTasks();
    void restoreTask(String title) throws TaskNotFoundException;
    void permanentlyDeleteTask(String title) throws TaskNotFoundException;
}
