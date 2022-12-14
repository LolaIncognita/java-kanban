package rent.model;

import java.util.Objects;

public class Task {
    String taskName;
    public String taskDescription;
    public Integer uniqueIdentificationNumber;
    protected String taskStatus;


    public Task() {
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String newTaskStatus) {
        taskStatus = newTaskStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(otherTask.uniqueIdentificationNumber, ((Task) obj).uniqueIdentificationNumber);
    }

}