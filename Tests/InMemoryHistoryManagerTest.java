import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.HistoryManager;
import rent.service.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import rent.service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.List;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    InMemoryTaskManager taskManager;
    Task task1;
    Task task2;
    Task task3;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        task1 = new Task(1, "Название задачи 1", "Описание по задаче 1", "NEW", LocalDateTime.of(2023,03,15,11,0), 15L);
        task2 = new Task(4, "Название задачи 2", "Описание по задаче 2", "NEW", LocalDateTime.of(2023,04,15,11,0), 15L);
        epic = new Epic(2, "Название эпика", "Описание по эпику", "NEW", LocalDateTime.of(2023,03,17,11,0), 15L);
        subtask = new Subtask(3, "Название по подзадаче", "Описание по подзадаче", "NEW", epic.getId(), LocalDateTime.of(2023,03,15,11,0), 15L);
        task3 = new Task(5, "Название задачи 3", "Описание по задаче 3", "NEW", LocalDateTime.of(2023,03,15,19,0), 15L);
    }

    @AfterEach
    void tearDown() {
        historyManager.getHistory().clear();
    }

    @Test
    void addOneTaskTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.getTaskById(1);
        Assertions.assertNotNull(historyManager.getHistory().size(), "История пустая.");
        Assertions.assertEquals(1, historyManager.getHistory().size(), "Размер истории отличается от ожидаемого.");
    }

    @Test
    void addTwoIdenticalTasksTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        Assertions.assertNotNull(historyManager.getHistory().size(), "История пустая.");
        Assertions.assertEquals(1, historyManager.getHistory().size(), "Размер истории отличается от ожидаемого.");
    }

    @Test
    void removeOneTaskFromEmptyListTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task2);
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
        historyManager.remove(task1.getId());
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void removeFirstTaskTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task2);
        taskManager.makeTaskByTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        int historySize = historyManager.getHistory().size();
        historyManager.remove(task1.getId());
        Assertions.assertEquals(historySize - 1, historyManager.getHistory().size(), "Размер истории отличается от ожидаемого.");
    }

    @Test
    void removeMiddleTaskTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task2);
        taskManager.makeTaskByTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        int historySize = historyManager.getHistory().size();
        historyManager.remove(task2.getId());
        Assertions.assertEquals(historySize - 1, historyManager.getHistory().size(), "Размер истории отличается от ожидаемого.");
    }

    @Test
    void removeLastTaskTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task2);
        taskManager.makeTaskByTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        int historySize = historyManager.getHistory().size();
        historyManager.remove(task3.getId());
        Assertions.assertEquals(historySize - 1, historyManager.getHistory().size(), "Размер истории отличается от ожидаемого.");
    }

    @Test
    void getHistoryFromEmptyListTest() {
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(0, history.size(),"История не пуста.");
    }

    @Test
    void getHistoryTest() {
        taskManager.makeTaskByTask(task1);
        taskManager.makeTaskByTask(task2);
        taskManager.makeTaskByTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(3, history.size(),"Ожидаемый размер истории не соответствует действительному.");
    }
}