package rent.model;
import rent.service.StatusOfTasks;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public StatusOfTasks getSubtaskStatus() {
        return taskStatus;
    }

    public void setSubtaskStatus(StatusOfTasks newStatus) {
        taskStatus = newStatus;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer newEpicId) {
        epicId = newEpicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherSubtask = (Task) obj;
        return Objects.equals(otherSubtask.id, this.id);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskId = '" + id + '\'' +
                ", taskName = '" + taskName + '\'' +
                ", taskStatus = '" + taskStatus + '\'' +
                ", taskDescribtion = '" + taskDescription +
                ", epicId = '" + epicId +
                '}';
    }
}