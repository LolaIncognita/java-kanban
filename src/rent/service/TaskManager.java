package rent.service;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    Task makeNewTask(Task task, String taskName, String description);

    Epic makeNewEpic(Epic epic, String taskName, String description);

    Subtask makeNewSubtask(Subtask subtask, Epic epic, String taskName,
                                  String description);

    ArrayList<Task> getTasksList();

    ArrayList<Epic> getEpicsList();

    ArrayList<Subtask> getSubtasksList();

    void clearTasksList();

    void clearEpicsList();

    void clearSubtasksList();

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    HashMap<Integer, Task> getAllTypeTasksIdList();

    ArrayList<Integer> getSubtaskIdOfEpic(Epic epic);

    void changeSubtaskStatus(Subtask subtask, StatusOfTasks newStatus);

    void updateTask(Integer taskId, Task newTask, String newTaskName, String newTaskDescription);

    void updataEpic(Integer epicId, Epic newEpic);

    void updateSubtask(Integer subtaskId, Subtask newSubtask);
}