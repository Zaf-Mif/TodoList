package view;

import controller.TaskManager;
import controller.TaskManagerImpl;
import exceptions.TaskNotFoundException;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodoListGUI {
    private JFrame frame;
    private JTextField titleField, descriptionField, actionTitleField;
    private JTextArea taskListArea;
    private TaskManager taskManager;

    public TodoListGUI() {
        taskManager = new TaskManagerImpl();
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 550);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);  // spacing around components

        // Title Label and Field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        // Description Label and Field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        // Add Task Button
        JButton addButton = new JButton("Add Task");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(addButton, gbc);

        // Action Title Label and Field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Task Title for Action:"), gbc);
        actionTitleField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(actionTitleField, gbc);

        // Remove Button
        JButton removeButton = new JButton("Remove Task");
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(removeButton, gbc);

        // Mark as Completed Button
        JButton markButton = new JButton("Mark as Completed");
        gbc.gridx = 1;
        panel.add(markButton, gbc);

        // Show All Tasks Button
        JButton showButton = new JButton("Show All Tasks");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(showButton, gbc);

        // Task List TextArea
        taskListArea = new JTextArea(15, 40);
        taskListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskListArea);
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String description = descriptionField.getText().trim();
                if (!title.isEmpty()) {
                    taskManager.addTask(new Task(title, description));
                    titleField.setText("");
                    descriptionField.setText("");
                    showTasks();
                } else {
                    JOptionPane.showMessageDialog(frame, "Title cannot be empty.");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = actionTitleField.getText().trim();
                try {
                    taskManager.removeTask(title);
                    actionTitleField.setText("");
                    showTasks();
                } catch (TaskNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Unexpected error: " + ex.getMessage());
                }
            }
        });

        markButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = actionTitleField.getText().trim();
                try {
                    taskManager.markTaskAsCompleted(title);
                    actionTitleField.setText("");
                    showTasks();
                } catch (TaskNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Unexpected error: " + ex.getMessage());
                }
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTasks();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showTasks() {
        StringBuilder sb = new StringBuilder();
        for (Task task : taskManager.getAllTasks()) {
            sb.append("Title: ").append(task.getTitle()).append("\n");
            sb.append("Description: ").append(task.getDescription()).append("\n");
            sb.append("Status: ").append(task.isCompleted() ? "✅ Completed" : "❌ Pending").append("\n");
            sb.append("-------------------------\n");
        }
        taskListArea.setText(sb.toString());
    }
}
