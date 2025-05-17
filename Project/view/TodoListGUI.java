package view;

import controller.TaskManager;
import controller.TaskManagerImpl;
import exceptions.TaskNotFoundException;
import model.Task;

import javax.swing.*;
import java.awt.*;

public class TodoListGUI {
    private JFrame frame;
    private JTextField titleField, descriptionField, actionTitleField;
    private JTextArea taskListArea, deletedTaskListArea;
    private TaskManager taskManager;

    public TodoListGUI() {
        taskManager = new TaskManagerImpl();
        frame = new JFrame("To-Do List with Recycle Bin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 600);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        JButton addButton = new JButton("Add Task");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(addButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Task Title for Action:"), gbc);
        actionTitleField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(actionTitleField, gbc);

        JButton removeButton = new JButton("Remove Task");
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(removeButton, gbc);
        JButton markButton = new JButton("Mark Completed");
        gbc.gridx = 1;
        panel.add(markButton, gbc);

        JButton showButton = new JButton("Show Tasks");
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(showButton, gbc);
        JButton showDeletedButton = new JButton("Show Recycle Bin");
        gbc.gridx = 1;
        panel.add(showDeletedButton, gbc);

        taskListArea = new JTextArea(10, 30);
        taskListArea.setEditable(false);
        deletedTaskListArea = new JTextArea(8, 30);
        deletedTaskListArea.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(taskListArea), gbc);

        gbc.gridy = 7;
        panel.add(new JScrollPane(deletedTaskListArea), gbc);

        gbc.gridy = 8; gbc.gridwidth = 1;
        JButton restoreButton = new JButton("Restore Task");
        panel.add(restoreButton, gbc);
        gbc.gridx = 1;
        JButton deletePermanentlyButton = new JButton("Delete Permanently");
        panel.add(deletePermanentlyButton, gbc);

        // Listeners
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descriptionField.getText().trim();
            if (!title.isEmpty()) {
                taskManager.addTask(new Task(title, desc));
                titleField.setText("");
                descriptionField.setText("");
                showTasks();
            } else {
                JOptionPane.showMessageDialog(frame, "Title cannot be empty.");
            }
        });

        removeButton.addActionListener(e -> performAction("remove"));
        markButton.addActionListener(e -> performAction("complete"));
        restoreButton.addActionListener(e -> performRecycleAction("restore"));
        deletePermanentlyButton.addActionListener(e -> performRecycleAction("delete"));
        showButton.addActionListener(e -> showTasks());
        showDeletedButton.addActionListener(e -> showDeletedTasks());

        frame.add(panel);
        frame.setVisible(true);
    }

    private void performAction(String action) {
        String title = actionTitleField.getText().trim();
        try {
            if (action.equals("remove")) taskManager.removeTask(title);
            else if (action.equals("complete")) taskManager.markTaskAsCompleted(title);
            actionTitleField.setText("");
            showTasks();
        } catch (TaskNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void performRecycleAction(String action) {
        String title = actionTitleField.getText().trim();
        try {
            if (action.equals("restore")) taskManager.restoreTask(title);
            else if (action.equals("delete")) taskManager.permanentlyDeleteTask(title);
            actionTitleField.setText("");
            showDeletedTasks();
            showTasks();
        } catch (TaskNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void showTasks() {
        StringBuilder sb = new StringBuilder();
        for (Task task : taskManager.getAllTasks()) {
            sb.append("Title: ").append(task.getTitle())
              .append(" | Desc: ").append(task.getDescription())
              .append(" | Status: ").append(task.isCompleted() ? "✅" : "❌").append("\n");
        }
        taskListArea.setText(sb.toString());
    }

    private void showDeletedTasks() {
        StringBuilder sb = new StringBuilder("🗑️ Recycle Bin:\n");
        for (Task task : taskManager.getDeletedTasks()) {
            sb.append("Title: ").append(task.getTitle())
              .append(" | Desc: ").append(task.getDescription()).append("\n");
        }
        deletedTaskListArea.setText(sb.toString());
    }
}
