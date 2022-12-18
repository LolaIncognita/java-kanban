package rent.model;
import rent.service.StatusOfTasks;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask() {
    }

    public StatusOfTasks getSubtaskStatus() {
        return taskStatus;
    }

    public void setSubtaskStatus(StatusOfTasks newStatus) {
        taskStatus = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherSubtask = (Task) obj;
        return Objects.equals(otherSubtask.id, ((Subtask) obj).id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId = '" + id + '\'' +
                ", taskName = '" + taskName + '\'' +
                ", taskStatus = '" + taskStatus + '\'' +
                ", taskDescribtion = '" + taskDescription +
                ", epicId = '" + epicId +
                '}';
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer newEpicId) {
        epicId = newEpicId;
    }
}