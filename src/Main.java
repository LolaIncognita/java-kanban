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

        // Блок для тестирования по ТЗ 5
        Task task1 = taskManager.makeNewTask(new Task(), "Название задачи 1", "Описание по задаче 1");
        Task task2 = taskManager.makeNewTask(new Task(), "Название задачи 2", "Описание по задаче 2");
        Epic epic1 = taskManager.makeNewEpic(new Epic(), "Название эпика 1", "Описание по эпику 1");
        Epic epic2 = taskManager.makeNewEpic(new Epic(), "Название эпика 2", "Описание по эпику 2");
        Subtask subtask2_1 = taskManager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.1",
                "Описание по подзадаче 2.1");
        Subtask subtask2_2 = taskManager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.2",
                "Описание по подзадаче 2.2");
        Subtask subtask2_3 = taskManager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.3",
                "Описание по подзадаче 2.3");

        System.out.println("Список задач всех типов " + taskManager.getAllTypeTasksIdList());
        //Просмотр истории - должна быть пустая
        historyManager.getHistory();

        //Получение объектов задача, подзадача, эпик по ID и просмотр после этого истории
        System.out.println("Информация по эпику №" + epic1.getId() + ": " + taskManager.getEpicById(epic1.getId()));
        historyManager.getHistory();
        System.out.println("Информация по эпику №" + epic2.getId() + ": " + taskManager.getEpicById(epic2.getId()));
        historyManager.getHistory();
        System.out.println("Информация по подзадаче №" + subtask2_2.getId() + ": "
                + taskManager.getSubtaskById(subtask2_2.getId()));
        historyManager.getHistory();
        System.out.println("Информация по подзадаче №" + subtask2_1.getId() + ": "
                + taskManager.getSubtaskById(subtask2_1.getId()));
        historyManager.getHistory();
        System.out.println("Информация по подзадаче №" + subtask2_3.getId() + ": "
                + taskManager.getSubtaskById(subtask2_3.getId()));
        historyManager.getHistory();

        System.out.println("Информация по задаче №" + task1.getId() + ": "
                + taskManager.getTaskById(task1.getId()));
        historyManager.getHistory();
        System.out.println("Информация по задаче №" + task2.getId() + ": "
                + taskManager.getTaskById(task2.getId()));
        historyManager.getHistory();

        //В истории не должно быть дублей. Задача при повторном вызове попадает снова в историю, предыдущий вызов из задачи удаляется
        System.out.println("Информация по эпику №" + epic1.getId() + ": " + taskManager.getEpicById(epic1.getId()));
        historyManager.getHistory();
        System.out.println("Информация по задаче №" + task1.getId() + ": "
                + taskManager.getTaskById(task1.getId()));
        historyManager.getHistory();


        //Удаление задачи по ID и просмотр после этого истории (удалённая задача не должна отобразиться в истории просмотра)
        taskManager.removeTaskById(task1.getId());
        System.out.println("Задача с уникальным идентификационным номером "
                + task1.getId() + " удалена.");
        historyManager.getHistory();

        //Удаление эпика с подзадачами по ID и просмотр после этого истории (удалённый эпик со всеми подзадачами
        // не должны отобразиться в истории просмотра)
        System.out.println("Удаление эпика");
        taskManager.removeEpicById(epic2.getId());
        System.out.println("Эпик с уникальным идентификационным номером "
                + epic2.getId() + " удалён.");
        historyManager.getHistory();

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
}