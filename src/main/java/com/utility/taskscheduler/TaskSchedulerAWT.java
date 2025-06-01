package com.utility.taskscheduler;

import com.utility.taskscheduler.model.Task;
import com.utility.taskscheduler.ui.TaskSchedulerFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskSchedulerAWT {
    private static List<Task> tasks = new ArrayList<>();
    private static long nextTaskId = 1;
    private static List<Long> remindedDueSoonTaskIds = new ArrayList<>();
    private static List<Long> remindedOverdueTaskIds = new ArrayList<>();
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TaskSchedulerFrame frame = new TaskSchedulerFrame();
                frame.setVisible(true);
                startReminderChecker(frame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public static List<Task> getTasks() {
        return tasks;
    }
    
    public static void addTask(Task task) {
        task.setTaskId(nextTaskId++);
        tasks.add(task);
    }
    
    public static void removeTask(Task task) {
        tasks.remove(task);
    }

    private static void startReminderChecker(TaskSchedulerFrame frame) {
        Thread reminderThread = new Thread(() -> {
            while (true) {
                try {
                    List<Task> tasks = getTasks();
                    long now = System.currentTimeMillis();
                    for (Task task : tasks) {
                        if (task.isReminderSet()) {
                            long diff = task.getDueDate().getTime() - now;
                            if (diff > 0 && diff <= 60 * 60 * 1000 && !remindedDueSoonTaskIds.contains(task.getTaskId())) {
                                java.text.SimpleDateFormat dtf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String msg = String.format("Task '%s' is due in less than an hour!\nDue: %s", task.getTaskName(), dtf.format(task.getDueDate()));
                                EventQueue.invokeLater(() -> frame.showMessage(msg));
                                remindedDueSoonTaskIds.add(task.getTaskId());
                            } else if (diff <= 0 && !remindedOverdueTaskIds.contains(task.getTaskId())) {
                                java.text.SimpleDateFormat dtf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String msg = String.format("Task '%s' is OVERDUE!\nDue: %s", task.getTaskName(), dtf.format(task.getDueDate()));
                                EventQueue.invokeLater(() -> frame.showMessage(msg));
                                remindedOverdueTaskIds.add(task.getTaskId());
                            }
                        }
                    }
                    Thread.sleep(60 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reminderThread.setDaemon(true);
        reminderThread.start();
    }
} 