import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rent.model.Task;
import rent.service.*;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagersTest extends TasksManagerTest <FileBackedTasksManagers> {
    File file;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        file = new File("src/rent/resources", "historyFile");
        taskManager = new FileBackedTasksManagers(historyManager, file);
        initTasks();
    }

    @Test
    void saveInFileTest() {
        String str = taskManager.readFile(file);
        String str3 = taskManager.readFile(file);
        Assertions.assertEquals(str, str3,"Состояние файлов не равны, записей в них при этом не делали.");
        Task taskForCheck = taskManager.makeNewTask(new Task("Название задачи для проверки", "Описание по задаче для проверки", LocalDateTime.of(2023,03,15,13,0), 15L));
        String str2 = taskManager.readFile(file);
        Assertions.assertNotEquals(str, str2,"Задача не записалась.");
    }

    @Test
    void readFileTest() {
        String str = taskManager.readFile(file);
        Assertions.assertNotNull(str, "Файл не прочитался.");
    }

    @Test
    void readFullFileTest() {
        file.delete();
        String[] str = new String[1];
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() {
                str[0] = taskManager.readFile(file);
            }
        });
        assertEquals("Произошла ошибка во время чтения файла", exception.getMessage());
        Assertions.assertNull(str[0], "Файл не пустой.");
    }

    @Test
    void loadWithEmptyHistoryFromFileTest() {
        FileBackedTasksManagers tasksManagers_2;
        tasksManagers_2 = FileBackedTasksManagers.loadFromFile(file);
        Assertions.assertEquals(taskManager.getAllTypeTasksIdList(), tasksManagers_2.getAllTypeTasksIdList());
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void loadWithHistoryFromFileTest() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        FileBackedTasksManagers tasksManagers_2;
        tasksManagers_2 = FileBackedTasksManagers.loadFromFile(file);
        Assertions.assertEquals(taskManager.getAllTypeTasksIdList(), tasksManagers_2.getAllTypeTasksIdList());
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void toStringTest() {
        String str = taskManager.toString(task1);
        Assertions.assertEquals("1,TASK,Название задачи 1,NEW,Описание по задаче 1,2023-03-15T11:00,15\n", str);
    }

    @Test
    void historyToStringOneTaskInHistory() {
        taskManager.getTaskById(1);
        String str = taskManager.historyToString(historyManager);
        Assertions.assertEquals("1", str);
    }

    @Test
    void historyToStringAnyTasksInHistory() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(6);
        String str = taskManager.historyToString(historyManager);
        Assertions.assertEquals("6,3,2,1", str);
    }

    @Test
    void historyToStringForEmptyHistory() {
        String str = taskManager.historyToString(historyManager);
        Assertions.assertEquals("", str);
    }

    @Test
    void historyFromStringTest() {
        String value = "6,3,2,1";
        List<Integer> historyFromString = taskManager.historyFromString(value);
        Assertions.assertNotNull(historyFromString, "Перечень задач из истории пуст.");
        Assertions.assertEquals(4, historyFromString.size(), "Размер перечня задачи из истории не соответствует ожидаемому.");
    }

    @Test
    void historyFromEmptyStringTest() {
        String value = "";
        List<Integer> historyFromString = taskManager.historyFromString(value);
        Assertions.assertEquals(0, historyFromString.size(), "Размер перечня задачи из истории не пуст.");
    }
}