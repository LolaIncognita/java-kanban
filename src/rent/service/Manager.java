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
    ArrayList<Task> tasksList = new ArrayList<>();
    ArrayList<Epic> epicsList = new ArrayList<>();
    ArrayList<Subtask> subtasksList = new ArrayList<>();

    public Task makeNewTask(Task task, String taskName, String description) {
        countOfTasks++;
        task.setId(countOfTasks);
        task.setTaskName(taskName);
        task.setTaskDescription(description);
        task.setTaskStatus(StatusOfTasks.NEW);
        allTasksMap.put(task.getId(), task);
        addTask(task);
        return task;
    }

    public Epic makeNewEpic(Epic epic, String taskName, String description) {
        countOfTasks++;
        epic.setId(countOfTasks);
        epic.setTaskName(taskName);
        epic.setTaskDescription(description);
        epic.setEpicStatus(StatusOfTasks.NEW);
        allTasksMap.put(epic.getId(), epic);
        addEpic(epic);
        return epic;
    }

    public Subtask makeNewSubtask(Subtask subtask, Epic epic, String taskName,
                                  String description) {
        countOfTasks++;
        subtask.setId(countOfTasks);
        subtask.setEpicId(epic.getId());
        subtask.setTaskName(taskName);
        subtask.setTaskDescription(description);
        subtask.setSubtaskStatus(StatusOfTasks.NEW);
        epic.joinSubtaskToEpic(epic, subtask);
        allTasksMap.put(subtask.getId(), subtask);
        addSubtask(subtask);
        return subtask;
    }

    public void addTask(Task task) {
        taskHashMap.put(task.getId(), task);
        tasksList.add(task);
    }

    public void addEpic(Epic epic) {
        epicsHashMap.put(epic.getId(), epic);
        epicsList.add(epic);
    }

    public void addSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
        subtasksList.add(subtask);
    }

    public ArrayList<Task> getTasksList() {
        return tasksList;
    }

    public ArrayList<Epic> getEpicsList() {
        return epicsList;
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void clearTasksList() {
        tasksList.clear();
    }

    public void clearEpicsList() {
        epicsList.clear();
    }

    public void clearSubtasksList() {
        subtasksList.clear();
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
        Epic epic = getEpicById(subtask.getEpicId());
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
        newTask.setId(taskId);
        Task task = getTaskById(taskId);
        newTask.setTaskStatus(task.getTaskStatus());
        newTask.setTaskName(task.getTaskName());
        newTask.setTaskDescription(task.getTaskDescription());
        removeTaskById(taskId);
        allTasksMap.put(newTask.getId(), newTask);
        taskHashMap.put(newTask.getId(), newTask);
    }

    public void updataEpic(Integer epicId, Epic newEpic) {
        newEpic.setId(epicId);
        Epic epic = getEpicById(epicId);
        newEpic.setEpicStatus(epic.getEpicStatus());
        newEpic.setTaskName(epic.getTaskName());
        newEpic.setTaskDescription(epic.getTaskDescription());
        removeEpicById(epicId);
        allTasksMap.put(newEpic.getId(), newEpic);
        epicsHashMap.put(newEpic.getId(), newEpic);
    }

    public void updateSubtask(Integer subtaskId, Subtask newSubtask) {
        newSubtask.setId(subtaskId);
        Subtask subtask = getSubtaskById(subtaskId);
        newSubtask.setSubtaskStatus(subtask.getTaskStatus());
        newSubtask.setEpicId(subtask.getEpicId());
        newSubtask.setTaskName(subtask.getTaskName());
        newSubtask.setTaskDescription(subtask.getTaskDescription());
        removeSubtaskById(subtaskId);
        allTasksMap.put(newSubtask.getId(), newSubtask);
        subtaskHashMap.put(newSubtask.getId(), newSubtask);
    }
}