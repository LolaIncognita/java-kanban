import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import rent.model.Task;
import rent.service.InMemoryTaskManager;
import rent.service.Managers;
import rent.service.ManagersSaveException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TasksManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        taskManager = new InMemoryTaskManager(historyManager);
        initTasks();
    }

    @Test
    public void isNotIntersectionTestTaskIntersection() {
        Boolean isNotIntersectionTask3 = taskManager.isNotIntersection(new Task("Название задачи 1", "Описание по задаче 1", LocalDateTime.of(2023,03,15,11,0), 15L));
        Assertions.assertFalse(isNotIntersectionTask3);
    }

    @Test
    public void isNotIntersectionTestTaskNotIntersection() {
        Boolean isNotIntersectionTask4 = taskManager.isNotIntersection(new Task("Название задачи 2", "Описание по задаче 2", LocalDateTime.of(2024,03,15,11,25), 15L));
        Assertions.assertTrue(isNotIntersectionTask4);
    }
}