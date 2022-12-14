package rent.model;
import rent.service.Manager;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> subtasksOfEpic;
    public Epic() {
        subtasksOfEpic = new ArrayList<>();
    }

    public void joinSubtaskToEpic(Epic joiningEpic, Subtask joiningSubtask) {
        joiningEpic.subtasksOfEpic.add(joiningSubtask.uniqueIdentificationNumber);
    }

    public ArrayList<Integer> getSubtasksOfEpic(Epic epic) {
        return epic.subtasksOfEpic;
    }

    public void setEpicStatus(String newStatus) {
        taskStatus = newStatus;
    }

    public String getEpicStatus() {
        return taskStatus;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(otherEpic.uniqueIdentificationNumber, ((Epic) obj).uniqueIdentificationNumber);
    }
}
