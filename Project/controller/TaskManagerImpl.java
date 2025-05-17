package controller;

import exceptions.TaskNotFoundException;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private List<Task> tasks;
    private List<Task> deletedTasks;
    private final String TASK_FILE = "tasks.ser";
    private final String DELETED_FILE = "deletedTasks.ser";

    public TaskManagerImpl() {
        tasks = loadTasks(TASK_FILE);
        deletedTasks = loadTasks(DELETED_FILE);
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);
        saveTasks(tasks, TASK_FILE);
    }

    @Override
    public void removeTask(String title) throws TaskNotFoundException {
        Task task = findTask(tasks, title);
        tasks.remove(task);
        deletedTasks.add(task);
        saveTasks(tasks, TASK_FILE);
        saveTasks(deletedTasks, DELETED_FILE);
    }

    @Override
    public void markTaskAsCompleted(String title) throws TaskNotFoundException {
        Task task = findTask(tasks, title);
        task.setCompleted(true);
        saveTasks(tasks, TASK_FILE);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Task> getDeletedTasks() {
        return new ArrayList<>(deletedTasks);
    }

    @Override
    public void restoreTask(String title) throws TaskNotFoundException {
        Task task = findTask(deletedTasks, title);
        deletedTasks.remove(task);
        tasks.add(task);
        saveTasks(tasks, TASK_FILE);
        saveTasks(deletedTasks, DELETED_FILE);
    }

    @Override
    public void permanentlyDeleteTask(String title) throws TaskNotFoundException {
        Task task = findTask(deletedTasks, title);
        deletedTasks.remove(task);
        saveTasks(deletedTasks, DELETED_FILE);
    }

    private Task findTask(List<Task> list, String title) throws TaskNotFoundException {
        return list.stream()
                .filter(t -> t.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + title));
    }

    private void saveTasks(List<Task> list, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Task> loadTasks(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Task>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
