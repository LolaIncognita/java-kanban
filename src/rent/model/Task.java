package rent.model;
import rent.service.StatusOfTasks;
import java.util.Objects;

public class Task {
    public String taskName;
    public String taskDescription;
    public Integer id;
    protected StatusOfTasks taskStatus;

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
}