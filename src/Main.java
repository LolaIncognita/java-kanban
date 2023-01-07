import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.HistoryManager;
import rent.service.StatusOfTasks;
import rent.service.TaskManager;
import rent.service.Managers;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        HistoryManager historyManager = managers.getDefaultHistory();

        // Блок для тестирования по ТЗ 4
        Task task1 = taskManager.makeNewTask(new Task(), "Название задачи 1", "Описание по задаче 1");
        Task task2 = taskManager.makeNewTask(new Task(), "Название задачи 2", "Описание по задаче 2");
        Epic epic1 = taskManager.makeNewEpic(new Epic(), "Название эпика 1", "Описание по эпику 1");
        Epic epic2 = taskManager.makeNewEpic(new Epic(), "Название эпика 2", "Описание по эпику 2");
        Subtask subtask1_1 = taskManager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.1",
                "Описание по подзадаче 1.1");
        Subtask subtask1_2 = taskManager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.2",
                "Описание по подзадаче 1.2");
        Subtask subtask2_1 = taskManager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.1",
                "Описание по подзадаче 2.1");
        System.out.println("Список задач всех типов " + taskManager.getAllTypeTasksIdList());

        Task forHistoryTask1 = taskManager.getTaskById(1);
        Epic forHistoryEpic1 = taskManager.getEpicById(3);
        Subtask forHistorySubtusk1 = taskManager.getSubtaskById(6);
        Task forHistoryTask2 = taskManager.getTaskById(2);
        Epic forHistoryEpic2 = taskManager.getEpicById(4);
        Subtask forHistorySubtusk2 = taskManager.getSubtaskById(5);
        Task forHistoryTask3 = taskManager.getTaskById(1);
        Epic forHistoryEpic3 = taskManager.getEpicById(3);
        Subtask forHistorySubtusk3 = taskManager.getSubtaskById(7);
        Task forHistoryTask4 = taskManager.getTaskById(1);
        Epic forHistoryEpic4 = taskManager.getEpicById(4);
        Subtask forHistorySubtusk4 = taskManager.getSubtaskById(6);

        // Блок для тестирвоания ТЗ 3
        //Создание задач, эпиков, подзадач
        /*Task task1 = taskManager.makeNewTask(new Task(), "Название задачи 1", "Описание по задаче 1");
        Task task2 = taskManager.makeNewTask(new Task(), "Название задачи 2", "Описание по задаче 2");
        Epic epic1 = taskManager.makeNewEpic(new Epic(), "Название эпика 1", "Описание по эпику 1");
        Epic epic2 = taskManager.makeNewEpic(new Epic(), "Название эпика 2", "Описание по эпику 2");
        Subtask subtask1_1 = taskManager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.1",
                "Описание по подзадаче 1.1");
        Subtask subtask1_2 = taskManager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.2",
                "Описание по подзадаче 1.2");
        Subtask subtask2_1 = taskManager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.1",
                "Описание по подзадаче 2.1");

        //Печать задач всех типов
        System.out.println("Список задач всех типов " + taskManager.getAllTypeTasksIdList());
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(taskManager.getTasksList());
        printEpicStatus(taskManager.getEpicsList());
        printSubtaskStatus(taskManager.getSubtasksList());
        //Изменение статуса двух подзадач на с NEW на DONE. Как поняла по задачнию у подзадач нет статуса IN_PROGRESS
        taskManager.changeSubtaskStatus(subtask1_1, StatusOfTasks.DONE);
        taskManager.changeSubtaskStatus(subtask2_1, StatusOfTasks.DONE);
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(taskManager.getTasksList());
        printEpicStatus(taskManager.getEpicsList());
        printSubtaskStatus(taskManager.getSubtasksList());
        //Обновление задач, эпиков и подзадач
        taskManager.updateTask(task2.getId(), new Task(),
                "Новое название по задаче 1", "Новое описание по задаче 1");
        taskManager.updataEpic(epic1.getId(), taskManager.makeNewEpic(new Epic(),
                "Новое название по эпику 3", "Новое описание по эпику 3"));
        taskManager.updateSubtask(subtask1_2.getId(), taskManager.makeNewSubtask(new Subtask(), epic1,
                "Новое название по подзадаче 6", "Новое описание по подзадаче 6"));
        //Удаление задачи и эпика по ID
        taskManager.removeTaskById(task1.getId());
        System.out.println("Задача с уникальным идентификационным номером "
                + task1.getId() + " удалена.");
        taskManager.removeEpicById(epic1.getId());
        System.out.println("Эпик с уникальным идентификационным номером "
                + epic1.getId() + " удалён.");
        taskManager.removeSubtaskById(subtask1_2.getId());
        System.out.println("Подзадача с уникальным идентификационным номером "
                + subtask1_2.getId() + " удалена.");
        //получение объектов: перечень эпиков, подзадач, задач, а также получение перечня подзадач по номеру эпика
        System.out.println("Коллекция задач: " + taskManager.getTasksList());
        System.out.println("Коллекция эпиков: " + taskManager.getEpicsList());
        System.out.println("Коллекция подзадач: " + taskManager.getSubtasksList());
        System.out.println("Список id подзадач эпика №" + epic1.getId() + ": " + taskManager.getSubtaskIdOfEpic(epic1));
        //Получение объектов задача, подзадача, эпик по ID
        System.out.println("Информация по эпику №" + epic1.getId() + ": " + taskManager.getEpicById(epic1.getId()));
        System.out.println("Информация по подзадаче №" + subtask1_2.getId() + ": "
                + taskManager.getSubtaskById(subtask1_2.getId()));
        System.out.println("Информация по задаче №" + task2.getId() + ": "
                + taskManager.getTaskById(task2.getId()));
        //Удаление всех задач, эпиков и подзадач
        taskManager.clearTasksList();
        System.out.println("Все задачи удалены.");
        taskManager.clearEpicsList();
        System.out.println("Все эпики удалены.");
        taskManager.clearSubtasksList();
        System.out.println("Все подзадачи удалены.");*/
        //Просмотр истории
        System.out.println("Смотрим историю:" + historyManager.getHistory());
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