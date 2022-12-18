package rent.model;
import rent.service.StatusOfTasks;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> subtaskIdOfEpic;

    public Epic() {
        subtaskIdOfEpic = new ArrayList<>();
    }

    public void joinSubtaskToEpic(Epic joiningEpic, Subtask joiningSubtask) {
        joiningEpic.subtaskIdOfEpic.add(joiningSubtask.id);
    }

    public ArrayList<Integer> getSubtasksOfEpic(Epic epic) {
        return epic.subtaskIdOfEpic;
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
                ", subtaskIdOfEpic = '" + subtaskIdOfEpic +
                '}';
    }
}