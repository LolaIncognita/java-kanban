package rent.model;
import rent.service.enums.StatusOfTasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> subtaskIdOfEpic;
    private LocalDateTime endTime;

    public Epic() {
        subtaskIdOfEpic = new ArrayList<>();
    }

    public Epic(Integer id, String taskName, String taskDescription, String taskStatus, LocalDateTime startTime, long duration) {
        super(id, taskName, taskDescription, taskStatus, startTime, duration);
        subtaskIdOfEpic = new ArrayList<>();
        endTime = null;
    }

    public Epic (String taskName, String taskDescription, LocalDateTime startTime, long duration) {
        super(taskName, taskDescription, startTime, duration);
        subtaskIdOfEpic = new ArrayList<>();
        endTime = null;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void joinSubtaskToEpic(Epic joiningEpic, Subtask joiningSubtask) {
        joiningEpic.subtaskIdOfEpic.add(joiningSubtask.id);
    }

    public ArrayList<Integer> getSubtasksOfEpic(Epic epic) {
        return epic.subtaskIdOfEpic;
    }

    public void setSubtasksOfEpic(ArrayList<Integer> subtaskIdOfEpic) {
        this.subtaskIdOfEpic = subtaskIdOfEpic;
    }

    public void setEpicStatus(StatusOfTasks newStatus) {
        taskStatus = newStatus;
    }

    public StatusOfTasks getEpicStatus() {
        return taskStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(otherEpic.id, this.id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId = '" + id + '\'' +
                ", taskName = '" + taskName + '\'' +
                ", taskStatus = '" + taskStatus + '\'' +
                ", taskDescribtion = '" + taskDescription +
                ", subtaskIdOfEpic = '" + subtaskIdOfEpic.toString() + '\'' +
                ", startTime = '" + startTime + '\'' +
                ", duration = '" + duration +
                "'}'";
    }
}