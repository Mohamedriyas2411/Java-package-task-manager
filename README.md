# Task Scheduler Application

A Spring Boot web application that allows users to manage scheduled tasks with reminders.

## Features

- View the current date and time
- Add, edit, and remove scheduled tasks
- Set tasks for specific dates and times
- Receive reminders for tasks that need to be completed
- Visual indicators for task due dates

## Technologies Used

- **Backend:** Spring Boot, Spring Data JPA, H2 Database
- **Frontend:** HTML, CSS, JavaScript
- **Java Features:** System, Date, and Calendar classes for time management

## How to Run

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run the `TaskSchedulerApplication` class
4. Access the application at `http://localhost:8080`

## API Endpoints

- `GET /api/tasks` - Returns a list of all scheduled tasks
- `GET /api/tasks/{id}` - Returns a specific task by ID
- `POST /api/addTask` - Creates a new task
- `PUT /api/updateTask` - Updates an existing task
- `DELETE /api/deleteTask/{id}` - Deletes a task
- `GET /api/currentTime` - Returns the current system date and time
- `GET /api/remainingTime/{id}` - Returns the time remaining for a specific task
- `GET /api/system-info` - Returns system-related information

## Core Java Classes Used

- **System** class - For system information and performance timing
- **Date** class - For handling current date and time
- **Calendar** class - For task scheduling and date calculations 