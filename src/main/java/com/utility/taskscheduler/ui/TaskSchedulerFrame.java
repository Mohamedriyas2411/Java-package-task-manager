package com.utility.taskscheduler.ui;

import com.utility.taskscheduler.TaskSchedulerAWT;
import com.utility.taskscheduler.model.Task;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class TaskSchedulerFrame extends Frame {
    private TextField taskNameField;
    private TextArea taskDescriptionArea;
    private TextField dueDateField;
    private Checkbox reminderCheckbox;
    private List taskList;
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Choice hourChoice;
    private Choice minuteChoice;
    private boolean isEditMode = false;
    private int editingIndex = -1;

    // Timer for countdown updates
    private Timer countdownTimer;

    public TaskSchedulerFrame() {
        setTitle("Task Scheduler");
        setSize(600, 500);
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (countdownTimer != null) countdownTimer.cancel();
                dispose();
                System.exit(0);
            }
        });

        // Start countdown updates
        startCountdownTimer();
    }


    private void initializeComponents() {
        Panel centerPanel = new Panel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Task Name
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new Label("Task Name:"), gbc);
        gbc.gridx = 1;
        taskNameField = new TextField(25);
        centerPanel.add(taskNameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new Label("Description:"), gbc);
        gbc.gridx = 1;
        taskDescriptionArea = new TextArea(3, 25);
        centerPanel.add(taskDescriptionArea, gbc);

        // Due Date
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new Label("Due Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        dueDateField = new TextField(12);
        centerPanel.add(dueDateField, gbc);

        // Time Picker
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new Label("Due Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        Panel timePanel = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hourChoice = new Choice();
        for (int i = 0; i < 24; i++) hourChoice.add(String.format("%02d", i));
        minuteChoice = new Choice();
        for (int i = 0; i < 60; i++) minuteChoice.add(String.format("%02d", i));
        timePanel.add(hourChoice);
        timePanel.add(new Label(":"));
        timePanel.add(minuteChoice);
        centerPanel.add(timePanel, gbc);

        // Reminder
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(new Label("Set Reminder:"), gbc);
        gbc.gridx = 1;
        reminderCheckbox = new Checkbox("Enable Reminder", true);
        centerPanel.add(reminderCheckbox, gbc);

        // Buttons row
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        addButton = new Button("Add Task");
        editButton = new Button("Edit Task");
        deleteButton = new Button("Delete Task");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(buttonPanel, gbc);

        // Add some vertical space below the form
        gbc.gridy = 6;
        centerPanel.add(new Label(" "), gbc);

        // Task List at the bottom
        taskList = new List(10);
        Panel listPanel = new Panel(new BorderLayout());
        listPanel.add(new Label("Tasks:"), BorderLayout.NORTH);
        listPanel.add(taskList, BorderLayout.CENTER);
        listPanel.setBackground(Color.WHITE);
        listPanel.setPreferredSize(new Dimension(580, 180));

        // Add to frame
        add(centerPanel, BorderLayout.CENTER);
        add(listPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addTask());
        editButton.addActionListener(e -> {
            if (!isEditMode) {
                startEditTask();
            } else {
                saveEditTask();
            }
        });
        deleteButton.addActionListener(e -> deleteTask());
    }

    private void setupLayout() {
        setLocationRelativeTo(null);
    }

    private void addTask() {
        if (isEditMode) {
            showMessage("Finish editing the current task or cancel before adding a new one.");
            return;
        }
        try {
            String taskName = taskNameField.getText();
            String description = taskDescriptionArea.getText();
            String dueDateStr = dueDateField.getText();
            String hourStr = hourChoice.getSelectedItem();
            String minuteStr = minuteChoice.getSelectedItem();
            boolean reminder = reminderCheckbox.getState();
            if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
                showMessage("Please enter a due date.");
                return;
            }
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dueDate = dateTimeFormat.parse(dueDateStr + " " + hourStr + ":" + minuteStr);
            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskDescription(description);
            task.setDueDate(dueDate);
            task.setReminderSet(reminder);
            task.setCreationDate(new Date());
            TaskSchedulerAWT.addTask(task);
            refreshList();
            clearInputFields();
        } catch (Exception ex) {
            showMessage("Error adding task: " + ex.getMessage());
        }
    }

    private void startEditTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a task to edit");
            return;
        }
        Task task = TaskSchedulerAWT.getTasks().get(selectedIndex);
        taskNameField.setText(task.getTaskName());
        taskDescriptionArea.setText(task.getTaskDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        dueDateField.setText(dateFormat.format(task.getDueDate()));
        hourChoice.select(hourFormat.format(task.getDueDate()));
        minuteChoice.select(minuteFormat.format(task.getDueDate()));
        reminderCheckbox.setState(task.isReminderSet());
        isEditMode = true;
        editingIndex = selectedIndex;
        editButton.setLabel("Save");
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void saveEditTask() {
        if (editingIndex == -1) {
            showMessage("No task selected for editing.");
            return;
        }
        try {
            Task task = TaskSchedulerAWT.getTasks().get(editingIndex);
            String taskName = taskNameField.getText();
            String description = taskDescriptionArea.getText();
            String dueDateStr = dueDateField.getText();
            String hourStr = hourChoice.getSelectedItem();
            String minuteStr = minuteChoice.getSelectedItem();
            boolean reminder = reminderCheckbox.getState();
            if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
                showMessage("Please enter a due date.");
                return;
            }
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dueDate = dateTimeFormat.parse(dueDateStr + " " + hourStr + ":" + minuteStr);
            task.setTaskName(taskName);
            task.setTaskDescription(description);
            task.setDueDate(dueDate);
            task.setReminderSet(reminder);
            refreshList();
            clearInputFields();
            isEditMode = false;
            editingIndex = -1;
            editButton.setLabel("Edit Task");
            addButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } catch (Exception ex) {
            showMessage("Error saving task: " + ex.getMessage());
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a task to delete");
            return;
        }
        Task task = TaskSchedulerAWT.getTasks().get(selectedIndex);
        TaskSchedulerAWT.removeTask(task);
        refreshList();
    }

    private void refreshList() {
        int selectedIndex = taskList.getSelectedIndex();

        taskList.removeAll();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int index = 1;
        Date now = new Date();

        for (Task task : TaskSchedulerAWT.getTasks()) {
            String countdownStr = getCountdownString(task.getDueDate(), now);
            String taskInfo = String.format("%d | %s | %s | %s | %s | %s",
                    index++,
                    task.getTaskName(),
                    task.getTaskDescription(),
                    dateTimeFormat.format(task.getDueDate()),
                    countdownStr,
                    task.isReminderSet() ? "Yes" : "No"
            );
            taskList.add(taskInfo);
        }
        if (selectedIndex >= 0 && selectedIndex < taskList.getItemCount()) {
            taskList.select(selectedIndex);
        }
    }

    private String getCountdownString(Date dueDate, Date now) {
        long diffMillis = dueDate.getTime() - now.getTime();
        if (diffMillis <= 0) {
            return "Time's up!";
        }
        long diffSeconds = diffMillis / 1000;
        long hours = diffSeconds / 3600;
        long minutes = (diffSeconds % 3600) / 60;
        long seconds = diffSeconds % 60;
        return String.format("%02d:%02d:%02d remaining", hours, minutes, seconds);
    }

    private void clearInputFields() {
        taskNameField.setText("");
        taskDescriptionArea.setText("");
        dueDateField.setText("");
        hourChoice.select(0);
        minuteChoice.select(0);
        reminderCheckbox.setState(true);
    }

    public void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new BorderLayout());
        Label label = new Label(message, Label.CENTER);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(label, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void startCountdownTimer() {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(() -> {
                    refreshList();
                });
            }
        }, 0, 1000);
    }
}
