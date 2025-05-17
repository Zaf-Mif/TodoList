// package controller;

// import model.Task;
// import exceptions.TaskNotFoundException;

// import java.util.ArrayList;
// import java.util.List;

// public class TaskManagerImpl implements TaskManager {
//     private List<Task> tasks;

//     public TaskManagerImpl() {
//         tasks = new ArrayList<>();
//     }

//     @Override
//     public void addTask(Task task) {
//         tasks.add(task);
//     }

//     @Override
//     public void removeTask(String title) throws TaskNotFoundException {
//         Task taskToRemove = findTaskByTitle(title);
//         if (taskToRemove != null) {
//             tasks.remove(taskToRemove);
//         } else {
//             throw new TaskNotFoundException("Task with title '" + title + "' not found.");
//         }
//     }

//     @Override
//     public void markTaskAsCompleted(String title) throws TaskNotFoundException {
//         Task taskToMark = findTaskByTitle(title);
//         if (taskToMark != null) {
//             taskToMark.setCompleted(true);
//         } else {
//             throw new TaskNotFoundException("Task with title '" + title + "' not found.");
//         }
//     }

//     @Override
//     public List<Task> getAllTasks() {
//         return tasks;
//     }

//     // Helper method
//     private Task findTaskByTitle(String title) {
//         for (Task task : tasks) {
//             if (task.getTitle().equalsIgnoreCase(title)) {
//                 return task;
//             }
//         }
//         return null;
//     }
// }
package controller;

import model.Task;
import exceptions.TaskNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private List<Task> tasks;
    private static final String DATA_FILE = "tasks.dat"; // file to save tasks

    public TaskManagerImpl() {
        tasks = loadTasks();  // Load tasks from file when created
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();  // Save tasks to file after adding
    }

    @Override
    public void removeTask(String title) throws TaskNotFoundException {
        Task taskToRemove = findTaskByTitle(title);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            saveTasks();  // Save tasks after removing
        } else {
            throw new TaskNotFoundException("Task with title '" + title + "' not found.");
        }
    }

    @Override
    public void markTaskAsCompleted(String title) throws TaskNotFoundException {
        Task taskToMark = findTaskByTitle(title);
        if (taskToMark != null) {
            taskToMark.setCompleted(true);
            saveTasks();  // Save tasks after marking completed
        } else {
            throw new TaskNotFoundException("Task with title '" + title + "' not found.");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    // Helper method to find a task by title
    private Task findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }

    // Save the list of tasks to a file
    private void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the list of tasks from a file
    @SuppressWarnings("unchecked")
    private List<Task> loadTasks() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if no file yet
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Task>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
