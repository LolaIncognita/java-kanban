import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.server.HttpTaskServer;
import rent.server.KVServer;
import rent.service.HttpTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TasksManagerTest<HttpTaskManager> {
    static KVServer kvServer;
    private String url;

    @BeforeAll
    static void totalSetUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        url = "http://localhost:";
        taskManager = new HttpTaskManager(url);
        taskManager.clearTasksList();
        taskManager.clearEpicsList();
        taskManager.clearSubtasksList();
        taskManager.getAllTypeTasksIdList().clear();
    }

    @Test
    private void saveAndLoadTest() {
        Task task1 = taskManager.makeNewTask(new Task("Название задачи 1", "Описание по задаче 1", LocalDateTime.of(2023,03,15,11,0), 15L));
        Task task2 = taskManager.makeNewTask(new Task("Название задачи 2", "Описание по задаче 2", LocalDateTime.of(2023,03,13,11,25), 15L));
        Epic epic1 = taskManager.makeNewEpic(new Epic("Название эпика 1", "Описание по эпику 1", LocalDateTime.of(2023,03,16,01,0), 15L));
        Epic epic2 = taskManager.makeNewEpic(new Epic("Название эпика 2", "Описание по эпику 2", LocalDateTime.of(2023,03,16,02,0), 15L));
        Subtask subtask2_1 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.1",
                "Описание по подзадаче 2.1", LocalDateTime.of(2023,03,16,02,0), 15L), epic2);
        Subtask subtask2_2 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.2",
                "Описание по подзадаче 2.2", LocalDateTime.of(2023,03,16,02,20), 15L), epic2);
        Subtask subtask2_3 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.3",
                "Описание по подзадаче 2.3", LocalDateTime.of(2023,03,16,02,50), 15L), epic2);

        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask2_2.getId());

        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        httpTaskManager.load();

        assertEquals(taskManager.getTasksList(), httpTaskManager.getTasksList());
        assertEquals(taskManager.getEpicsList(), httpTaskManager.getEpicsList());
        assertEquals(taskManager.getSubtasksList(), httpTaskManager.getSubtasksList());
        assertEquals(taskManager.getHistoryManager().getHistory(), httpTaskManager.getHistoryManager().getHistory());
        kvServer.stop();
    }

    @Test
    void correctLoadTask() {
        Task task1 = taskManager.makeNewTask(new Task("Название задачи 1", "Описание по задаче 1", LocalDateTime.of(2023,03,15,11,0), 15L));
        Task task2 = taskManager.makeNewTask(new Task("Название задачи 2", "Описание по задаче 2", LocalDateTime.of(2023,03,13,11,25), 15L));
        taskManager.makeNewTask(task1);
        taskManager.makeNewTask(task2);
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task1.getId());
        List<Task> history = taskManager.getHistoryManager().getHistory();
        List<Task> historyForCheck = new ArrayList<>();
        historyForCheck.add(task1);
        historyForCheck.add(task2);
        assertEquals(historyForCheck, history);
    }

    @Test
    void correctLoadEpic() {
        Epic epic1 = taskManager.makeNewEpic(new Epic("Название эпика 1", "Описание по эпику 1", LocalDateTime.of(2023,03,16,01,0), 15L));
        Epic epic2 = taskManager.makeNewEpic(new Epic("Название эпика 2", "Описание по эпику 2", LocalDateTime.of(2023,03,16,02,0), 15L));
        taskManager.makeNewEpic(epic1);
        taskManager.makeNewEpic(epic2);
        taskManager.getEpicById(epic2.getId());
        taskManager.getEpicById(epic1.getId());
        List<Task> history = taskManager.getHistoryManager().getHistory();
        List<Epic> historyForCheck = new ArrayList<>();
        historyForCheck.add(epic1);
        historyForCheck.add(epic2);
        assertEquals(historyForCheck, history);
    }

    @Test
    void correctLoadSubtask() {
        Epic epic2 = taskManager.makeNewEpic(new Epic("Название эпика 2", "Описание по эпику 2", LocalDateTime.of(2023, 03, 16, 02, 0), 15L));
        Subtask subtask2_1 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.1",
                "Описание по подзадаче 2.1", LocalDateTime.of(2023, 03, 16, 02, 0), 15L), epic2);
        Subtask subtask2_2 = taskManager.makeNewSubtask(new Subtask("Название по подзадаче 2.2",
                "Описание по подзадаче 2.2", LocalDateTime.of(2023, 03, 16, 02, 20), 15L), epic2);
        taskManager.makeNewSubtask(subtask2_1, epic2);
        taskManager.makeNewSubtask(subtask2_2, epic2);
        taskManager.getSubtaskById(subtask2_1.getId());
        taskManager.getSubtaskById(subtask2_2.getId());
        List<Task> history = taskManager.getHistoryManager().getHistory();
        List<Subtask> historyForCheck = new ArrayList<>();
        historyForCheck.add(subtask2_2);
        historyForCheck.add(subtask2_1);
        assertEquals(historyForCheck, history);
    }
}
