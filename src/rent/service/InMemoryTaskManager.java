package rent.service;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    public static int countOfTasks = 0;
    HashMap<Integer, Task> allTasksMap = new HashMap<>();
    ArrayList<Task> tasksList = new ArrayList<>();
    ArrayList<Epic> epicsList = new ArrayList<>();
    ArrayList<Subtask> subtasksList = new ArrayList<>();
    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Task makeNewTask(Task task, String taskName, String description) {
        countOfTasks++;
        task.setId(countOfTasks);
        task.setTaskName(taskName);
        task.setTaskDescription(description);
        task.setTaskStatus(StatusOfTasks.NEW);
        allTasksMap.put(task.getId(), task);
        tasksList.add(task);
        return task;
    }

    @Override
    public Epic makeNewEpic(Epic epic, String taskName, String description) {
        countOfTasks++;
        epic.setId(countOfTasks);
        epic.setTaskName(taskName);
        epic.setTaskDescription(description);
        epic.setEpicStatus(StatusOfTasks.NEW);
        allTasksMap.put(epic.getId(), epic);
        epicsList.add(epic);
        return epic;
    }

    @Override
    public Subtask makeNewSubtask(Subtask subtask, Epic epic, String taskName, String description) {
        countOfTasks++;
        subtask.setId(countOfTasks);
        subtask.setEpicId(epic.getId());
        subtask.setTaskName(taskName);
        subtask.setTaskDescription(description);
        subtask.setSubtaskStatus(StatusOfTasks.NEW);
        epic.joinSubtaskToEpic(epic, subtask);
        allTasksMap.put(subtask.getId(), subtask);
        subtasksList.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return tasksList;
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return epicsList;
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    @Override
    public void clearTasksList() {
        tasksList.clear();
    }

    @Override
    public void clearEpicsList() {
        epicsList.clear();
    }

    @Override
    public void clearSubtasksList() {
        subtasksList.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task taskById = null;
        for (Task task: tasksList) {
            if (id.equals(task.getId())) {
                 taskById = task;
            }
        }
        historyManager.addHistory(taskById);
        return taskById;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = null;
        for (Epic epic: epicsList) {
            if (id.equals(epic.getId())) {
                epicById = epic;
            }
        }
        historyManager.addHistory(epicById);
        return epicById;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtaskById = null;
        for (Subtask subtask: subtasksList) {
            if (id.equals(subtask.getId())) {
                subtaskById = subtask;
            }
        }
        historyManager.addHistory(subtaskById);
        return subtaskById;
    }

    @Override
    public void removeTaskById(Integer id) {
        int countOfRemovings = 0;
        ArrayList<Task> list = new ArrayList<>();

        historyManager.removeHistory(id);
        for (Task task: tasksList) {
            if (task.getId().equals(id)) {
                list.add(task);
                countOfRemovings += 1;
            }
        }

        if (countOfRemovings == 0) {
            System.out.println("Задача с id = " + id + " не найдена.");
        } else {
            allTasksMap.remove(id);
            tasksList.remove(list);
        }
    }

    @Override
    public void removeEpicById(Integer id) {
        int countOfRemovings = 0;
        ArrayList<Epic> list = new ArrayList<>();
        Epic epic = getEpicById(id);
        ArrayList<Integer> subtasksIdOfEpic = epic.getSubtasksOfEpic(epic);
        if (subtasksIdOfEpic.size() > 0) {
            for (Integer subtaskId : subtasksIdOfEpic) {
                historyManager.removeHistory(subtaskId);
            }
        }
        historyManager.removeHistory(id);
        for (Epic epicForRemove: epicsList) {
            if ((epicForRemove.getId()).equals(id)) {
                list.add(epicForRemove);
                countOfRemovings += 1;
            }
        }

        if (countOfRemovings == 0) {
            System.out.println("Задача с id = " + id + " не найдена.");
        } else {
            epicsList.remove(list);
            allTasksMap.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        int countOfRemovings = 0;
        ArrayList<Subtask> list = new ArrayList<>();

        historyManager.removeHistory(id);
        for (Subtask subtask: subtasksList) {
            if (subtask.getId().equals(id)) {
                list.add(subtask);
                countOfRemovings += 1;
            }
        }
        if (countOfRemovings == 0) {
            System.out.println("Подзадача с id = " + id + " не найдена.");
        } else {
            subtasksList.remove(list);
            allTasksMap.remove(id);
        }
    }

    @Override
    public HashMap<Integer, Task> getAllTypeTasksIdList() {
        return allTasksMap;
    }

    @Override
    public ArrayList<Integer> getSubtaskIdOfEpic(Epic epic) {
        ArrayList<Integer> subtasksIdOfEpic = epic.getSubtasksOfEpic(epic);
        return subtasksIdOfEpic;
    }

    @Override
    public void changeSubtaskStatus(Subtask subtask, StatusOfTasks newStatus) {
        subtask.setSubtaskStatus(newStatus);
        Epic epic = getEpicById(subtask.getEpicId());
        ArrayList<StatusOfTasks> subtaskStatusOfEpic = new ArrayList<>();
        ArrayList<Integer> subtaskID = epic.getSubtasksOfEpic(epic);
        for (int i = 0; i < subtaskID.size(); i++) {
            Subtask otherSubtask = getSubtaskById(subtaskID.get(i));
            subtaskStatusOfEpic.add(otherSubtask.getSubtaskStatus());
        }
        if (subtaskStatusOfEpic.contains(StatusOfTasks.NEW) && subtaskStatusOfEpic.contains(StatusOfTasks.DONE)) {
            epic.setEpicStatus(StatusOfTasks.IN_PROGRESS);
        } else if (!subtaskStatusOfEpic.contains(StatusOfTasks.NEW)) {
            epic.setEpicStatus(StatusOfTasks.DONE);
        }
    }

    @Override
    public void updateTask(Integer taskId, Task newTask, String newTaskName, String newTaskDescription) {
        newTask.setId(taskId);
        Task task = getTaskById(taskId);
        newTask.setTaskStatus(task.getTaskStatus());
        newTask.setTaskName(newTaskName);
        newTask.setTaskDescription(newTaskDescription);
        tasksList.remove(task);
        allTasksMap.remove(taskId);
        allTasksMap.put(newTask.getId(), newTask);
        tasksList.add(newTask);
    }

    @Override
    public void updataEpic(Integer epicId, Epic newEpic) {
        newEpic.setId(epicId);
        Epic epic = getEpicById(epicId);
        newEpic.setEpicStatus(epic.getEpicStatus());
        newEpic.setTaskName(epic.getTaskName());
        newEpic.setTaskDescription(epic.getTaskDescription());
        epicsList.remove(epic);
        allTasksMap.remove(epicId);
        allTasksMap.put(newEpic.getId(), newEpic);
        epicsList.add(newEpic);
    }

    @Override
    public void updateSubtask(Integer subtaskId, Subtask newSubtask) {
        newSubtask.setId(subtaskId);
        Subtask subtask = getSubtaskById(subtaskId);
        newSubtask.setSubtaskStatus(subtask.getTaskStatus());
        newSubtask.setEpicId(subtask.getEpicId());
        newSubtask.setTaskName(subtask.getTaskName());
        newSubtask.setTaskDescription(subtask.getTaskDescription());
        subtasksList.remove(subtask);
        allTasksMap.remove(subtaskId);
        allTasksMap.put(newSubtask.getId(), newSubtask);
        subtasksList.add(newSubtask);
    }
}
