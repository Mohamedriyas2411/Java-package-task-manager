package com.utility.taskscheduler.model;

import java.util.Date;

public class Task {
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private Date dueDate;
    private boolean isReminderSet = true;
    private Date creationDate;

    public Task() {
        this.isReminderSet = true;
        this.creationDate = new Date();
    }

    public Task(Long taskId, String taskName, String taskDescription, Date dueDate, boolean isReminderSet, Date creationDate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.isReminderSet = true;
        this.creationDate = creationDate;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isReminderSet() {
        return isReminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        this.isReminderSet = reminderSet;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", dueDate=" + dueDate +
                ", isReminderSet=" + isReminderSet +
                ", creationDate=" + creationDate +
                '}';
    }
}