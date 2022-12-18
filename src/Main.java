import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.Manager;
import rent.service.StatusOfTasks;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
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
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(manager.getTasksList());
        printEpicStatus(manager.getEpicsList());
        printSubtaskStatus(manager.getSubtasksList());
        //Изменение статуса двух подзадач на с NEW на DONE. Как поняла по задачнию у подзадач нет статуса IN_PROGRESS
        manager.changeSubtaskStatus(subtask1_1, StatusOfTasks.DONE);
        manager.changeSubtaskStatus(subtask2_1, StatusOfTasks.DONE);
        //Печать статусов всех задач, эпиков, подзадач по группам
        printTaskStatus(manager.getTasksList());
        printEpicStatus(manager.getEpicsList());
        printSubtaskStatus(manager.getSubtasksList());
        //Обновление задач, эпиков и подхадач
        manager.updateTask(task2.getId(), new Task(),
                "Новое название по задаче 1", "Новое описание по задаче 1");
        manager.updataEpic(epic1.getId(), manager.makeNewEpic(new Epic(),
                "Новое название по эпику 3", "Новое описание по эпику 3"));
        manager.updateSubtask(subtask1_2.getId(), manager.makeNewSubtask(new Subtask(), epic1,
                "Новое название по подзадаче 6", "Новое описание по подзадаче 6"));
        //Удаление задачи и эпика по ID
        manager.removeTaskById(task1.getId());
        System.out.println("Задача с уникальным идентификационным номером "
                + task1.getId() + " удалена.");
        manager.removeEpicById(epic1.getId());
        System.out.println("Эпик с уникальным идентификационным номером "
                + epic1.getId() + " удалён.");
        manager.removeSubtaskById(subtask1_2.getId());
        System.out.println("Подзадача с уникальным идентификационным номером "
                + subtask1_2.getId() + " удалена.");
        //получение объектов: перечень эпиков, подзадач, задач, а также получение перечня подзадач по номеру эпика
        System.out.println("Коллекция задач: " + manager.getTasksList());
        System.out.println("Коллекция эпиков: " + manager.getEpicsList());
        System.out.println("Коллекция подзадач: " + manager.getSubtasksList());
        System.out.println("Список id подзадач эпика №" + epic1.getId() + ": " + manager.getSubtaskIdOfEpic(epic1));
        //Получение объектов задача, подзадача, эпик по ID
        System.out.println("Информация по эпику №" + epic1.getId() + ": " + manager.getEpicById(epic1.getId()));
        System.out.println("Информация по подзадаче №" + subtask1_2.getId() + ": "
                + manager.getSubtaskById(subtask1_2.getId()));
        System.out.println("Информация по задаче №" + task2.getId() + ": "
                + manager.getTaskById(task2.getId()));
        //Удаление всех задач, эпиков и подзадач
        manager.clearTasksList();
        System.out.println("Все задачи удалены.");
        manager.clearEpicsList();
        System.out.println("Все эпики удалены.");
        manager.clearSubtasksList();
        System.out.println("Все подзадачи удалены.");
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