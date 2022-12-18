import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.Manager;
import rent.service.StatusOfTasks;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Epic> epicsHashMap;
        HashMap<Integer, Task> taskHashMap;
        HashMap<Integer, Subtask> subtaskHashMap;

        System.out.println("Поехали!");
        Manager manager = new Manager();
        //Создание задач, эпиков, подзадач
        Task task1 = manager.makeNewTask(new Task(), "Название задачи 1", "Описание по задаче 1");
        Task task2 = manager.makeNewTask(new Task(), "Название задачи 2", "Описание по задаче 2");
        Epic epic1 = manager.makeNewEpic(new Epic(), "Название эпика 1", "Описание по эпику 1");
        Epic epic2 = manager.makeNewEpic(new Epic(), "Название эпика 2", "Описание по эпику 2");
        Subtask subtask1_1 = manager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.1",
                "Описание по подзадаче 1.1");
        Subtask subtask1_2 = manager.makeNewSubtask(new Subtask(), epic1, "Название по подзадаче 1.2",
                "Описание по подзадаче 1.2");
        Subtask subtask2_1 = manager.makeNewSubtask(new Subtask(), epic2, "Название по подзадаче 2.1",
                "Описание по подзадаче 2.1");
        //Печать задач всех типов
        System.out.println("Список задач всех типов " + manager.getAllTypeTasksIdList());
        //Печать всех задач, эпиков, подзадач по группам
        taskHashMap = manager.getTasksIdList();
        prinTaskList(taskHashMap);
        epicsHashMap = manager.getEpicsList();
        printEpicList(epicsHashMap);
        subtaskHashMap = manager.getSubtasksList();
        printSubtaskList(subtaskHashMap);
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(taskHashMap);
        printEpicStatus(epicsHashMap);
        printSubtaskStatus(subtaskHashMap);
        //Изменение статуса двух подзадач на с NEW на DONE. Как поняла по задачнию у подзадач нет статуса IN_PROGRESS
        manager.changeSubtaskStatus(subtask1_1, StatusOfTasks.DONE);
        manager.changeSubtaskStatus(subtask2_1, StatusOfTasks.DONE);
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(taskHashMap);
        printEpicStatus(epicsHashMap);
        printSubtaskStatus(subtaskHashMap);
        //Обновление задач, эпиков и подхадач
        manager.updateTask(task2.getId(), manager.makeNewTask(new Task(),
                "Новое название по задаче 1", "Новое описание по задаче 1"));
        manager.updataEpic(epic1.getId(), manager.makeNewEpic(new Epic(),
                "Новое название по эпику 3", "Новое описание по эпику 3"));
        manager.updateSubtask(subtask1_2.getId(), manager.makeNewSubtask(new Subtask(), epic1,
                "Новое название по подзадаче 6", "Новое описание по подзадаче 6"));
        //Удаление задачи и эпика по ID
        manager.removeTaskById(task1.getId());
        manager.removeEpicById(epic1.getId());
        //получение объектов: перечень эпиков, подзадач, тазач, а также получение перечня подзадач по номеру эпика
        manager.getEpicsList();
        manager.getSubtasksList();
        manager.getTasksIdList();
        manager.getSubtaskIdOfEpic(epic1);
        //Получение объектов задача, подзадача, эпик по ID
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1_2.getId());
        manager.getTaskById(task2.getId());
        //Удаление всех задач, эпиков и подзадач
        manager.clearTasksList();
        System.out.println("Все задачи удалены.");
        manager.clearEpicsList();
        System.out.println("Все эпики удалены.");
        manager.clearSubtasksList();
        System.out.println("Все подзадачи удалены.");
    }

    public static void printTaskStatus(HashMap<Integer, Task> taskHashMap) {
        if(taskHashMap.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Статусы задач:");
            for (Integer taskId : taskHashMap.keySet()) {
                    Task task = taskHashMap.get(taskId);
                    System.out.println("Задача №" + taskId + ": "
                            + task.getTaskStatus() + ".");
            }
        }
    }

    public static void printSubtaskStatus(HashMap<Integer, Subtask> subtaskHashMap) {
        if(subtaskHashMap.isEmpty()) {
            System.out.println("Список подзадач пуст.");
        } else {
            System.out.println("Статусы подзадач:");
            for (Integer subtaskId : subtaskHashMap.keySet()) {
                    Subtask subtask = subtaskHashMap.get(subtaskId);
                    System.out.println("Подзадача №" + subtaskId + ": "
                            + subtask.getTaskStatus() + ".");
            }
        }
    }

    public static void printEpicStatus(HashMap<Integer, Epic> epicsHashMap) {
        System.out.println("Статусы эпиков:");
        for (Integer epicId : epicsHashMap.keySet()) {
                Epic epic = epicsHashMap.get(epicId);
                System.out.println("Эпик №" + epicId + ": "
                        + epic.getEpicStatus() + ".");
        }
    }

    public static void printSubtaskList(HashMap<Integer, Subtask> subtaskHashMap) {
        System.out.println("Список подзадач:");
        for (Integer subtaskId : subtaskHashMap.keySet()) {
            System.out.println("Подзадача №" + subtaskId + ": "
                    + subtaskHashMap.get(subtaskId) + ".");
        }
    }

    public static void printEpicList(HashMap<Integer, Epic> epicsHashMap) {
       System.out.println("Список эпиков:");
       for (Integer epicId : epicsHashMap.keySet()) {
           System.out.println("Эпик №" + epicId + ": "
                   + epicsHashMap.get(epicId) + ".");
       }
   }

    public static void prinTaskList(HashMap<Integer, Task> taskHashMap) {
        System.out.println("Список задач:");
        for (Integer taskId : taskHashMap.keySet()) {
            System.out.println("Задача №" + taskId + ": "
                    + taskHashMap.get(taskId) + ".");
        }
    }
}