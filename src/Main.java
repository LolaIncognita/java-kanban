import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.HistoryManager;
import rent.service.TaskManager;
import rent.service.Managers;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);
    }

    public static void printTaskStatus(ArrayList<Task> tasksList) {
        if(tasksList.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Статусы задач:");
            for (Task task : tasksList) {
                System.out.println("Задача №" + task.getId() + ": "
                        + task.getTaskStatus() + ".");
            }
        }
    }

    public static void printSubtaskStatus(ArrayList<Subtask> subtasksList) {
        if(subtasksList.isEmpty()) {
            System.out.println("Список подзадач пуст.");
        } else {
            System.out.println("Статусы подзадач:");
            for (Subtask subtask : subtasksList) {
                    System.out.println("Подзадача №" + subtask.getId() + ": "
                            + subtask.getTaskStatus() + ".");
            }
        }
    }

    public static void printEpicStatus(ArrayList<Epic> epicsList) {
        System.out.println("Статусы эпиков:");
        for (Epic epic : epicsList) {
                System.out.println("Эпик №" + epic.getId() + ": "
                        + epic.getEpicStatus() + ".");
        }
    }

    public static void printHistory(HistoryManager historyManager) {
        System.out.println("Смотрим историю:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}