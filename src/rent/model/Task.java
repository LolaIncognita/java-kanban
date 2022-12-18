package rent.model;
import rent.service.StatusOfTasks;
import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected Integer id;
    protected StatusOfTasks taskStatus;

    public Task() {
    }

    public StatusOfTasks getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(StatusOfTasks newTaskStatus) {
        taskStatus = newTaskStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(otherTask.id, ((Task) obj).id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId = '" + id + '\'' +
                ", taskName = '" + taskName + '\'' +
                ", taskStatus = '" + taskStatus + '\'' +
                ", taskDescribtion = '" + taskDescription +
                '}';
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
}