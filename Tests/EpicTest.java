import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rent.model.Epic;
import rent.model.Subtask;
import rent.service.HistoryManager;
import rent.service.Managers;
import rent.service.StatusOfTasks;
import rent.service.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class EpicTest {
    HistoryManager historyManager;
    TaskManager taskManager;
    Epic epic;
    List<Subtask> subtasks = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault(historyManager);
        epic = taskManager.makeNewEpic(new Epic("Название эпика", "Описание по эпику", LocalDateTime.of(2023,03,15,11,0), 15L));
    }

    public Subtask makeSubtasksNew(int i) {
        return taskManager.makeNewSubtask(new Subtask("Название по подзадаче " + i,
                        "Описание по подзадаче " + i, LocalDateTime.of(2023,03,15 + i,11,0), 15L),  epic);
    }


    //Для расчёта статуса Epic. Граничные условия:
    //a.   Пустой список подзадач.
    @Test
    void witchStatusOfEpicTestWithoutSubtasks() {
        Assertions.assertEquals(StatusOfTasks.NEW, epic.getEpicStatus());
    }

    //b.   Все подзадачи со статусом NEW.
    @Test
    void witchStatusOfEpicTestWithSubtasksNew() {
        for (int i = 1; i <= 3; i++) {
            subtasks.add(makeSubtasksNew(i));
        }
        Assertions.assertEquals(StatusOfTasks.NEW, epic.getEpicStatus());
    }

    //c.    Все подзадачи со статусом DONE.
    @Test
    void witchStatusOfEpicTestWithSubtasksDone() {
        for (int i = 1; i <= 3; i++) {
            subtasks.add(makeSubtasksNew(i));
            taskManager.changeSubtaskStatus(subtasks.get(i-1), StatusOfTasks.DONE);
        }
        Assertions.assertEquals(StatusOfTasks.DONE, epic.getEpicStatus());
    }

    // d.    Подзадачи со статусами NEW и DONE.
    @Test
    void witchStatusOfEpicTestWithSubtaskNewAndsDone() {
        for (int i = 1; i <= 3; i++) {
            subtasks.add(makeSubtasksNew(i));
            if(i % 2 == 1) {
                taskManager.changeSubtaskStatus(subtasks.get(i-1), StatusOfTasks.DONE);
            }
        }
        Assertions.assertEquals(StatusOfTasks.IN_PROGRESS, epic.getEpicStatus());
    }

    //e.    Подзадачи со статусом IN_PROGRESS.
    @Test
    void witchStatusOfEpicTestWithSubtaskInProgress() {
        for (int i = 1; i <= 3; i++) {
            subtasks.add(makeSubtasksNew(i));
            taskManager.changeSubtaskStatus(subtasks.get(i-1), StatusOfTasks.IN_PROGRESS);
        }
        Assertions.assertEquals(StatusOfTasks.IN_PROGRESS, epic.getEpicStatus());
    }

}