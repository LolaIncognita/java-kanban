package rent.model;
import rent.service.StatusOfTasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected Integer id;
    protected StatusOfTasks taskStatus;
    protected long duration;
    protected LocalDateTime startTime;
    public Task () {

    }
    public Task (Integer id, String taskName, String taskDescription, String taskStatus, LocalDateTime startTime, long duration) {
        this.id = id;
        this.taskName = taskName;
        this.taskStatus = StatusOfTasks.valueOf(taskStatus);
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task (String taskName, String taskDescription, LocalDateTime startTime, long duration) {
        this.id = null;
        this.taskName = taskName;
        this.taskStatus = StatusOfTasks.NEW;
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public StatusOfTasks getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(StatusOfTasks newTaskStatus) {
        taskStatus = newTaskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String newTaskName) {
        taskName = newTaskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String newTaskDescription) {
        taskDescription = newTaskDescription;
    }

    public void setId(Integer newId) {
        id = newId;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(otherTask.id, this.id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId = '" + id + '\'' +
                ", taskName = '" + taskName + '\'' +
                ", taskStatus = '" + taskStatus + '\'' +
                ", taskDescribtion = '" + taskDescription + '\'' +
                ", startTime = '" + startTime + '\'' +
                ", duration = '" + duration +
                '}';
    }
}