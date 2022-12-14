import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.Manager;

import java.util.HashMap;


public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Epic> epicsHashMap;
        HashMap<Integer, Task> taskHashMap;
        HashMap<Integer, Subtask> subtaskHashMap;

        System.out.println("Поехали!");
        Manager manager = new Manager();

        //Создание задач, эпиков, подзадач
        Task task1 = manager.makeNewTask(new Task(), "Описание по задаче 1");
        Task task2 = manager.makeNewTask(new Task(), "Описание по задаче 2");
        Epic epic1 = manager.makeNewEpic(new Epic(), "Описание по эпику 1");
        Epic epic2 = manager.makeNewEpic(new Epic(), "Описание по эпику 2");
        Subtask subtask1_1 = manager.makeNewSubtask(new Subtask(), epic1, "Описание по подзадаче 1.1");
        Subtask subtask1_2 = manager.makeNewSubtask(new Subtask(), epic1, "Описание по подзадаче 1.2");
        Subtask subtask2_1 = manager.makeNewSubtask(new Subtask(), epic2, "Описание по подзадаче 2.1");

        //Печать задач всех типов
        System.out.println("Список задач всех типов " + manager.getAllTypeTasksMap());
        //Печать всех задач, эпиков, подзадач по группам
        taskHashMap = manager.getTasksList();
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
        manager.changeSubtaskStatus(subtask1_1, "DONE");
        manager.changeSubtaskStatus(subtask2_1, "DONE");

        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(taskHashMap);
        printEpicStatus(epicsHashMap);
        printSubtaskStatus(subtaskHashMap);

        //Обновление задач, эпиков и подхадач
        manager.updateTask(task2.uniqueIdentificationNumber, manager.makeNewTask(new Task(),
                "Новое описание по задаче 1"));
        manager.updataEpic(epic1.uniqueIdentificationNumber, manager.makeNewEpic(new Epic(),
                "Новое описание по эпику 3"));
        manager.updateSubtask(subtask1_2.uniqueIdentificationNumber, manager.makeNewSubtask(new Subtask(), epic1,
                "Новое описание по подзадаче 6"));

        //Удаление задачи и эпика по ID
        manager.removeTaskByIdentifyCod(task1.uniqueIdentificationNumber);
        manager.removeEpicByIdentifyCod(epic1.uniqueIdentificationNumber);

        //получение объектов: перечень эпиков, подзадач, тазач, а также получение перечня подзадач по номеру эпика
        manager.getEpicsList();
        manager.getSubtasksList();
        manager.getTasksList();
        manager.getSubtaskOfEpic(epic1);

        //Получение объектов задача, подзадача, эпик по ID
        manager.getEpicByIdentifyCod(epic1.uniqueIdentificationNumber);
        manager.getSubtaskByIdentifyCod(subtask1_2.uniqueIdentificationNumber);
        manager.getTaskByIdentifyCod(task2.uniqueIdentificationNumber);

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
            for (Integer uniqueIdentificationNumber : taskHashMap.keySet()) {
                    Task task = taskHashMap.get(uniqueIdentificationNumber);
                    System.out.println("Задача №" + uniqueIdentificationNumber + ": "
                            + task.getTaskStatus() + ".");
            }
        }
    }
    public static void printSubtaskStatus(HashMap<Integer, Subtask> subtaskHashMap) {
        if(subtaskHashMap.isEmpty()) {
            System.out.println("Список подзадач пуст.");
        } else {
            System.out.println("Статусы подзадач:");
            for (Integer uniqueIdentificationNumber : subtaskHashMap.keySet()) {
                    Subtask subtask = subtaskHashMap.get(uniqueIdentificationNumber);
                    System.out.println("Подзадача №" + uniqueIdentificationNumber + ": "
                            + subtask.getTaskStatus() + ".");
            }
        }
    }

    public static void printEpicStatus(HashMap<Integer, Epic> epicsHashMap) {
        System.out.println("Статусы эпиков:");
        for (Integer uniqueIdentificationNumber : epicsHashMap.keySet()) {
                Epic epic = epicsHashMap.get(uniqueIdentificationNumber);
                System.out.println("Эпик №" + uniqueIdentificationNumber + ": "
                        + epic.getEpicStatus() + ".");
        }
    }

    public static void printSubtaskList(HashMap<Integer, Subtask> subtaskHashMap) {
        System.out.println("Список подзадач:");
        for (Integer uniqueIdentificationNumber : subtaskHashMap.keySet()) {
            System.out.println("Подзадача №" + uniqueIdentificationNumber + ": "
                    + subtaskHashMap.get(uniqueIdentificationNumber) + ".");
        }
    }

   public static void printEpicList(HashMap<Integer, Epic> epicsHashMap) {
       System.out.println("Список эпиков:");
       for (Integer uniqueIdentificationNumber : epicsHashMap.keySet()) {
           System.out.println("Эпик №" + uniqueIdentificationNumber + ": "
                   + epicsHashMap.get(uniqueIdentificationNumber) + ".");
       }
   }

    public static void prinTaskList(HashMap<Integer, Task> taskHashMap) {
        System.out.println("Список задач:");
        for (Integer uniqueIdentificationNumber : taskHashMap.keySet()) {
            System.out.println("Задача №" + uniqueIdentificationNumber + ": "
                    + taskHashMap.get(uniqueIdentificationNumber) + ".");
        }
    }
}
