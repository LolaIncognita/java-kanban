package rent.service;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public static int countOfTasks = 0;
    HashMap<Integer, Task> allTasksMap = new HashMap<>();
    HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public Task makeNewTask(Task task, String taskName, String description) {
        countOfTasks++;
        task.id = countOfTasks;
        task.taskName = taskName;
        task.taskDescription = description;
        task.setTaskStatus(StatusOfTasks.NEW);
        allTasksMap.put(task.id, task);
        addTask(task);
        return task;
    }

    public Epic makeNewEpic(Epic epic, String taskName, String description) {
        countOfTasks++;
        epic.id = countOfTasks;
        epic.taskName = taskName;
        epic.taskDescription = description;
        epic.setEpicStatus(StatusOfTasks.NEW);
        allTasksMap.put(epic.id, epic);
        addEpic(epic);
        return epic;
    }

    public Subtask makeNewSubtask(Subtask subtask, Epic epic, String taskName,
                                  String description) {
        countOfTasks++;
        subtask.id = countOfTasks;
        subtask.epicId = epic.id;
        subtask.taskName = taskName;
        subtask.taskDescription = description;
        subtask.setSubtaskStatus(StatusOfTasks.NEW);
        epic.joinSubtaskToEpic(epic, subtask);
        allTasksMap.put(subtask.id, subtask);
        addSubtask(subtask);
        return subtask;
    }

    public void addTask(Task task) {
        taskHashMap.put(task.id, task);
    }

    public void addEpic(Epic epic) {
        epicsHashMap.put(epic.id, epic);
    }

    public void addSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.id, subtask);
    }

    public HashMap<Integer, Task> getTasksIdList() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicsList() {
        return epicsHashMap;
    }

    public HashMap<Integer, Subtask> getSubtasksList() {
        return subtaskHashMap;
    }

    public void clearTasksList() {
        taskHashMap.clear();
    }

    public void clearEpicsList() {
        epicsHashMap.clear();
    }

    public void clearSubtasksList() {
        subtaskHashMap.clear();
    }

    public Task getTaskById(Integer id) {
        Task task = taskHashMap.get(id);
        return task;
    }

    public Epic getEpicById(Integer id) {
        Epic epic = epicsHashMap.get(id);
        return epic;
    }

    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtaskHashMap.get(id);
        return subtask;
    }

    public void removeTaskById(Integer id) {
        taskHashMap.remove(id);
        allTasksMap.remove(id);
        System.out.println("Задача с уникальным идентификационным номером "
                + id + " удалена.");
    }

    public void removeEpicById(Integer id) {
        epicsHashMap.remove(id);
        allTasksMap.remove(id);
        System.out.println("Эпик с уникальным идентификационным номером "
                + id + " удалён.");
    }

    public void removeSubtaskById(Integer identifyCod) {
        subtaskHashMap.remove(identifyCod);
        allTasksMap.remove(identifyCod);
        System.out.println("Подзадача с уникальным идентификационным номером "
                + identifyCod + " удалена.");
    }

    public HashMap<Integer, Task> getAllTypeTasksIdList() {
        return allTasksMap;
    }

    public ArrayList<Integer> getSubtaskIdOfEpic(Epic epic) {
        ArrayList<Integer> subtasksIdOfEpic = epic.getSubtasksOfEpic(epic);
        return subtasksIdOfEpic;
    }

    public void changeSubtaskStatus(Subtask subtask, StatusOfTasks newStatus) {
        subtask.setSubtaskStatus(newStatus);
        Epic epic = getEpicById(subtask.epicId);
        ArrayList<StatusOfTasks> subtaskStatusOfEpic = new ArrayList<>();
        ArrayList<Integer> subtaskID = epic.getSubtasksOfEpic(epic);

        for(int i = 0; i < subtaskID.size(); i++) {
            Subtask otherSubtask = getSubtaskById(subtaskID.get(i));
            subtaskStatusOfEpic.add(otherSubtask.getSubtaskStatus());
            }
        if(subtaskStatusOfEpic.contains(StatusOfTasks.NEW) &&
                subtaskStatusOfEpic.contains(StatusOfTasks.DONE)) {
            epic.setEpicStatus(StatusOfTasks.IN_PROGRESS);
        } else if (!subtaskStatusOfEpic.contains(StatusOfTasks.NEW)) {
            epic.setEpicStatus(StatusOfTasks.DONE);
        }
    }

    public void updateTask(Integer taskId, Task newTask) {
        newTask.id = taskId;
        Task task = getTaskById(taskId);
        newTask.setTaskStatus(task.getTaskStatus());
        removeTaskById(taskId);
        allTasksMap.put(newTask.id, newTask);
        taskHashMap.put(newTask.id, newTask);
    }

    public void updataEpic(Integer epicId, Epic newEpic) {
        newEpic.id = epicId;
        Epic epic = getEpicById(epicId);
        newEpic.setEpicStatus(epic.getEpicStatus());
        removeEpicById(epicId);
        allTasksMap.put(newEpic.id, newEpic);
        epicsHashMap.put(newEpic.id, newEpic);
    }

    public void updateSubtask(Integer subtaskId, Subtask newSubtask) {
        newSubtask.id = subtaskId;
        Subtask subtask = getSubtaskById(subtaskId);
        newSubtask.setSubtaskStatus(subtask.getTaskStatus());
        newSubtask.epicId = subtask.epicId;
        removeSubtaskById(subtaskId);
        allTasksMap.put(newSubtask.id, newSubtask);
        subtaskHashMap.put(newSubtask.id, newSubtask);
    }
}