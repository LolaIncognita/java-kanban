import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.*;
import rent.service.enums.StatusOfTasks;
import rent.service.historyManager.HistoryManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

abstract class TasksManagerTest<T extends TaskManager> {
    HistoryManager historyManager;
    T taskManager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask2_1;
    Subtask subtask2_2;
    Subtask subtask2_3;

    void initTasks() {
        taskManager.clearSubtasksList();
        taskManager.clearEpicsList();
        taskManager.clearTasksList();
        taskManager.getAllTypeTasksIdList().clear();
        InMemoryTaskManager.countOfTasks = 0;
        task1 = taskManager.makeNewTask(new Task("Название задачи 1", "Описание по задаче 1", LocalDateTime.of(2023,03,15,11,0), 15L));
        task2 = taskManager.makeNewTask(new Task("Название задачи 2", "Описание по задаче 2", LocalDateTime.of(2023,03,13,11,25), 15L));
        epic1 = taskManager.makeNewEpic(new Epic("Название эпика 1", "Описание по эпику 1", LocalDateTime.of(2023,03,16,01,0), 15L));
        epic2 = taskManager.makeNewEpic(new Epic("Название эпика 2", "Описание по эпику 2", LocalDateTime.of(2023,03,16,02,0), 15L));
        subtask2_1 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.1",
                "Описание по подзадаче 2.1", LocalDateTime.of(2023,03,16,02,0), 15L), epic2);
        subtask2_2 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.2",
                "Описание по подзадаче 2.2", LocalDateTime.of(2023,03,16,02,20), 15L), epic2);
        subtask2_3 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.3",
                "Описание по подзадаче 2.3", LocalDateTime.of(2023,03,16,02,50), 15L), epic2);
    }

    // метод makeTaskByTask не тестирую, т.к. он в функционале треккера не предполагается, создан под тесты

    @Test
    public void getPrioritizedTasksTest() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> listForCheck = new ArrayList<>();
        listForCheck.add(task2);
        listForCheck.add(task1);
        listForCheck.add(subtask2_1);
        listForCheck.add(subtask2_2);
        listForCheck.add(subtask2_3);
        Assertions.assertEquals(listForCheck, prioritizedTasks);
    }

    @Test
    public void makeNewTaskTest() {
        Task taskForCheck = new Task(1,"Название задачи 1", "Описание по задаче 1", "NEW", LocalDateTime.of(2023,03,15,11,0), 15L);
        Assertions.assertEquals(taskForCheck, task1, "Созданная задача не соответствует ожидаемой.");
        Assertions.assertNotNull(task1, "Задача не найдена.");
    }

    @Test
    public void makeNewEpicTest() {
        Epic epicForCheck = new Epic(3,"Название эпика 1", "Описание по эпику 1", "NEW", LocalDateTime.of(2023,03,16,01,0), 15L);
        Assertions.assertNotNull(epic1, "Эпик не найден.");
        Assertions.assertEquals(epicForCheck, epic1, "Созданный эпик не соответствует ожидаемому.");
    }

    @Test
    public void makeNewSubtaskTest() {
        Subtask subtaskForCheck = new Subtask(7,"Название по подзадаче 2.3", "Описание по подзадаче 2.3", "NEW", 2, LocalDateTime.of(2023,03,16,02,50), 15L);
        Assertions.assertEquals(subtaskForCheck, subtask2_3, "Созданная подзадача не соответствует ожидаемой.");
        Assertions.assertNotNull(task1, "Подзадача не найдена.");
    }

    //удалила проверку геттеров

    @Test
    public void clearTasksListTest() {
        Task taskForCheck1 = new Task(1, "Название задачи 1", "Описание по задаче 1", "NEW", LocalDateTime.of(2023,03,15,11,0), 15L);
        Task taskForCheck2 = new Task(2, "Название задачи 2", "Описание по задаче 2", "NEW", LocalDateTime.of(2023,03,15,11,25), 15L);
        List<Task> tasksListForCheck = new ArrayList<>();
        tasksListForCheck.add(taskForCheck1);
        tasksListForCheck.add(taskForCheck2);
        Assertions.assertEquals(tasksListForCheck, taskManager.getTasksList(), "Список задач (перед очисткой) отличается от ожидаемого.");
        taskManager.clearTasksList();
        tasksListForCheck.clear();
        Assertions.assertEquals(tasksListForCheck, taskManager.getTasksList(), "Список задач содержит задачу/задачи.");
    }

    @Test
    public void clearEpicsListTest() {
        Epic epicForCheck1 = new Epic(3, "Название эпика 1", "Описание по эпику 1", "NEW", LocalDateTime.of(2023,03,16,01,0), 15L);
        Epic epicForCheck2 = new Epic(4, "Название эпика 2", "Описание по эпику 2", "NEW", LocalDateTime.of(2023,03,16,02,0), 15L);
        List<Epic> epicsListForCheck = new ArrayList<>();
        epicsListForCheck.add(epicForCheck1);
        epicsListForCheck.add(epicForCheck2);
        Assertions.assertEquals(epicsListForCheck, taskManager.getEpicsList(), "Список эпиков (перед очисткой) отличается от ожидаемого.");
        taskManager.clearEpicsList();
        epicsListForCheck.clear();
        Assertions.assertEquals(epicsListForCheck, taskManager.getEpicsList(), "Список эпиков содержит эпик/эпики.");
    }

    @Test
    public void clearSubtasksListTest() {
        Subtask subtuskForCheck1 = new Subtask(5, "Название по подзадаче 2.1", "Описание по подзадаче 2.1", "NEW", 4, LocalDateTime.of(2023,03,16,02,0), 15L);
        Subtask subtuskForCheck2 = new Subtask(6, "Название по подзадаче 2.2", "Описание по подзадаче 2.2", "NEW", 4, LocalDateTime.of(2023,03,16,02,20), 15L);
        Subtask subtuskForCheck3 = new Subtask(7, "Название по подзадаче 2.3", "Описание по подзадаче 2.3", "NEW", 4, LocalDateTime.of(2023,03,16,02,50), 15L);
        List<Subtask> subtasksListForCheck = new ArrayList<>();
        subtasksListForCheck.add(subtuskForCheck1);
        subtasksListForCheck.add(subtuskForCheck2);
        subtasksListForCheck.add(subtuskForCheck3);
        Assertions.assertEquals(subtasksListForCheck, taskManager.getSubtasksList(), "Список подзадач (перед очисткой) отличается от ожидаемого.");
        taskManager.clearSubtasksList();
        subtasksListForCheck.clear();
        Assertions.assertEquals(subtasksListForCheck, taskManager.getSubtasksList(), "Список подзадач содержит подзадачи/подзадачу.");
    }

    //не стала ставить такую проверку: Assertions.assertEquals(task1, taskManager.getTaskById(task1.getId())), т.к.
    //получилось бы, что я использую метод getID, который в свою очередь не протестирован. Аналогично в проверке
    // getByID для эпиков и сабтасков
    @Test
    public void getTaskByIdTest() {
        Task taskForCheck = new Task(1, "Название задачи 1", "Описание по задаче 1", "NEW", LocalDateTime.of(2023,03,15,11,0), 15L);
        Assertions.assertNotNull(taskManager.getTaskById(1), "Задача не найдена");
        Assertions.assertEquals(taskForCheck, taskManager.getTaskById(1), "Найденная по ID задача не соответствует ожидаемой.");
    }

    @Test
    public void getEpicByIdTest() {
        Epic epicForCheck1 = new Epic(3, "Название эпика 1", "Описание по эпику 1", "NEW", LocalDateTime.of(2023,03,16,01,0), 15L);
        Assertions.assertNotNull(taskManager.getEpicById(3), "Эпик не найдена");
        Assertions.assertEquals(epicForCheck1, taskManager.getEpicById(3), "Найденная по ID задача не соответствует ожидаемой.");
    }

    @Test
    public void getSubtaskByIdTest() {
        Subtask subtuskForCheck1 = new Subtask(5, "Название по подзадаче 2.1", "Описание по подзадаче 2.1", "NEW", 4, LocalDateTime.of(2023,03,16,02,0), 15L);
        Assertions.assertNotNull(taskManager.getSubtaskById(5), "Подзадача не найдена");
        Assertions.assertEquals(subtuskForCheck1, taskManager.getSubtaskById(5), "Найденная по ID подзадача не соответствует ожидаемой.");
    }

    @Test
    public void removeTaskByIdTest() {
        Assertions.assertTrue(taskManager.getAllTypeTasksIdList().containsKey(1), "Перечень всех задач не содержит искомой задачи.");
        taskManager.removeTaskById(1);
        Assertions.assertFalse(taskManager.getAllTypeTasksIdList().containsKey(1), "Задача не была удалена из перечня всех задач.");
    }

    @Test
    public void removeEpicByIdTest() {
        Assertions.assertTrue(taskManager.getAllTypeTasksIdList().containsKey(3), "Перечень всех задач не содержит искомого эпика.");
        taskManager.removeEpicById(3);
        Assertions.assertFalse(taskManager.getAllTypeTasksIdList().containsKey(3), "Эпик не была удалён из перечня всех задач.");
    }

    @Test
    public void removeSubtaskByIdTest() {
        Assertions.assertTrue(taskManager.getAllTypeTasksIdList().containsKey(5), "Перечень всех задач не содержит искомой подзадачи.");
        taskManager.removeSubtaskById(5);
        Assertions.assertFalse(taskManager.getAllTypeTasksIdList().containsKey(5), "Подзадача не была удалена из перечня всех задач.");
    }

    @Test
    public void getAllTypeTasksIdListTest() {
        HashMap<Integer, Task> allTasksMap = new HashMap<>();
        allTasksMap.put(1, task1);
        allTasksMap.put(2, task2);
        allTasksMap.put(3, epic1);
        allTasksMap.put(4, epic2);
        allTasksMap.put(5, subtask2_1);
        allTasksMap.put(6, subtask2_2);
        allTasksMap.put(7, subtask2_3);
        Assertions.assertNotNull(taskManager.getAllTypeTasksIdList(), "Перечень всех задач пуст.");
        Assertions.assertEquals(allTasksMap, taskManager.getAllTypeTasksIdList(), "Перечень всех задач не соответствует ожидаемому.");
    }

    @Test
    public void getSubtaskIdOfEpicTest() {
        List<Integer> subtaskIdOfEpic2 = new ArrayList<>();
        subtaskIdOfEpic2.add(5);
        subtaskIdOfEpic2.add(6);
        subtaskIdOfEpic2.add(7);
        Assertions.assertEquals(subtaskIdOfEpic2, taskManager.getSubtaskIdOfEpic(epic2), "");
    }

    @Test
    public void changeSubtaskStatusTest() {
        Assertions.assertEquals(StatusOfTasks.NEW, subtask2_1.getSubtaskStatus());
        taskManager.changeSubtaskStatus(subtask2_1, StatusOfTasks.IN_PROGRESS);
        Assertions.assertEquals(StatusOfTasks.IN_PROGRESS, subtask2_1.getSubtaskStatus());
    }

    @Test
    public void updateTaskTest() {
        Task taskForChek = new Task(1, "Новое имя для задачи 1", "Новое описание для задачи 1", "NEW", LocalDateTime.of(2023,03,15,11,0), 15L);
        taskManager.updateTask(1, new Task(), "Новое имя для задачи 1", "Новое описание для задачи 1");
        Assertions.assertEquals(taskForChek, task1);
    }

    @Test
    public void updateEpicTest() {
        Epic newEpic = new Epic();
        Epic epicForChek = new Epic();
        epicForChek.setTaskName("Новое имя для задачи 2");
        epicForChek.setTaskDescription("Новое описание для задачи 2");
        epicForChek.setId(4);
        // попробовала везде переделать на List, чтобы переменная была в классе Epic с типом List и геттеры с сеттерами
        // принимали/выдавали лист, но везде как-то сильно начало ругаться, и в итоге здесь оставила Array
        ArrayList<Integer> subtaskIdOfEpic2 = new ArrayList<>();
        subtaskIdOfEpic2.add(5);
        subtaskIdOfEpic2.add(6);
        subtaskIdOfEpic2.add(7);
        epicForChek.setSubtasksOfEpic(subtaskIdOfEpic2);
        epicForChek.setTaskStatus(epic2.getTaskStatus());
        taskManager.updateEpic(epic2.getId(), newEpic);
        Assertions.assertEquals(epicForChek, epic2);
    }

    @Test
    public void updateSubtask() {
        Subtask newSubtask = new Subtask();
        Subtask subtaskForChek = new Subtask();
        subtaskForChek.setTaskName("Новое имя для подзадачи 3");
        subtaskForChek.setTaskDescription("Новое описание для подзадачи 3");
        subtaskForChek.setId(7);
        subtaskForChek.setEpicId(4);
        subtaskForChek.setTaskStatus(subtask2_3.getTaskStatus());
        taskManager.updateSubtask(subtask2_3.getId(), newSubtask);
        Assertions.assertEquals(subtaskForChek, subtask2_3);
    }

    //Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса (расчёт статуса
    // представлен в EpicTest
    @Test
    public void hasSubtaskEpicTest() {
        int epicId = subtask2_1.getEpicId();
        Assertions.assertNotNull(epicId, "Отсутствует инофрмация по ID эпика по подзадаче.");
        Assertions.assertEquals(4,epicId, "ID эпика по подзадаче не соответствует ожидаемому.");
    }
}
