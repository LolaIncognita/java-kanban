package rent.model;
import java.util.HashMap;
import java.util.Objects;

public class Subtask extends Task {
    HashMap<Integer, Integer> tasksMap = new HashMap<>();
    public Integer epicID;

    public Subtask() {
    }

    public String getSubtaskStatus() {
        return taskStatus;
    }
    public void setSubtaskStatus(String newStatus) {
        taskStatus = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherSubtask = (Task) obj;
        return Objects.equals(otherSubtask.uniqueIdentificationNumber, ((Subtask) obj).uniqueIdentificationNumber);
    }
}
