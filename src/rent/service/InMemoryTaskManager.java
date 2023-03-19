package rent.service;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.enums.StatusOfTasks;
import rent.service.historyManager.HistoryManager;

import java.time.LocalDateTime;
import java.util.*;

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
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Task makeNewTask(Task task) {
        if(task != null) {
            if (isNotIntersection(task)) {
                countOfTasks++;
                task.setId(countOfTasks);
                allTasksMap.put(task.getId(), task);
                tasksList.add(task);
                return task;
            } else {
                System.out.println("Задача не добавлена: пересечение во времени по подзадачам.");
            }
        }
        Task taskNull = new Task();
        return taskNull;
    }

    public void makeTaskByTask(Task task) {
        if(task != null) {
            if (isNotIntersection(task)) {
                countOfTasks++;
                task.setId(task.getId());
                task.setTaskName(task.getTaskName());
                task.setTaskDescription(task.getTaskDescription());
                task.setTaskStatus(task.getTaskStatus());
                allTasksMap.put(task.getId(), task);
                tasksList.add(task);
            } else {
                System.out.println("Пересечение во времени по задачам. Задаче не присвоен id.");
            }
        }
    }

    @Override
    public Epic makeNewEpic(Epic epic) {
        countOfTasks++;
        epic.setId(countOfTasks);
        allTasksMap.put(epic.getId(), epic);
        epicsList.add(epic);
        return epic;
    }

    @Override
    public Subtask makeNewSubtask(Subtask subtask, Epic epic) {
        if(subtask != null) {
            if (isNotIntersection(subtask)) {
                countOfTasks++;
                subtask.setId(countOfTasks);
                subtask.setEpicId(epic.getId());
                epic.joinSubtaskToEpic(epic, subtask);
                allTasksMap.put(subtask.getId(), subtask);
                subtasksList.add(subtask);
                checkEpicStartAndEndTime(epic);
                return subtask;
            } else {
                System.out.println("Подзадача не добавлена: пересечение во времнни по подзадачам.");
            }
        }
       return null;
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

        historyManager.remove(id);
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
                historyManager.remove(subtaskId);
            }
        }
        historyManager.remove(id);
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
        Epic epic = getEpicById(getSubtaskById(id).getEpicId());
        historyManager.remove(id);
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
        checkEpicStartAndEndTime(epic);
        updateEpicStatus(epic);
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
        updateEpicStatus(epic);
    }

    protected void updateEpicStatus(Epic epic) {
        ArrayList<StatusOfTasks> subtaskStatusOfEpic = new ArrayList<>();
        ArrayList<Integer> subtaskId = epic.getSubtasksOfEpic(epic);
        for (int i = 0; i < subtaskId.size(); i++) {
            Subtask otherSubtask = getSubtaskById(subtaskId.get(i));
            subtaskStatusOfEpic.add(otherSubtask.getSubtaskStatus());
        }
        if (subtaskStatusOfEpic.contains(StatusOfTasks.NEW) && subtaskStatusOfEpic.contains(StatusOfTasks.DONE)) {
            epic.setEpicStatus(StatusOfTasks.IN_PROGRESS);
        } else if (!subtaskStatusOfEpic.contains(StatusOfTasks.NEW) && !subtaskStatusOfEpic.contains(StatusOfTasks.IN_PROGRESS)) {
            epic.setEpicStatus(StatusOfTasks.DONE);
        } else {
            epic.setEpicStatus(StatusOfTasks.IN_PROGRESS);
        }
    }

    public void checkEpicStartAndEndTime(Epic epic) {
        if (epic.getSubtasksOfEpic(epic).isEmpty()) {
            return;
        }
        long epicDuration = 0;
        LocalDateTime epicStartTime = LocalDateTime.MAX;
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        for (Integer subtaskId : epic.getSubtasksOfEpic(epic)) {
            Subtask subtask = getSubtaskByIdWithoutSaveInHistory(subtaskId);
            epicDuration += subtask.getDuration();
            if (epicStartTime.isAfter(subtask.getStartTime())) {
                epicStartTime = subtask.getStartTime();
            }
            if (epicEndTime.isBefore(subtask.getEndTime())) {
                epicEndTime = subtask.getEndTime();
            }
        }
        epic.setDuration(epicDuration);
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
    }

    public Subtask getSubtaskByIdWithoutSaveInHistory(Integer id) {
        Subtask subtaskById = null;
        for (Subtask subtask: subtasksList) {
            if (id.equals(subtask.getId())) {
                subtaskById = subtask;
            }
        }
        return subtaskById;
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
    public void updateEpic(Integer epicId, Epic newEpic) {
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

    @Override
    public List<Task> getPrioritizedTasks() {
        TreeSet<Task> set = new TreeSet<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null)
                    return Integer.compare(o1.getId(), o2.getId());
                if (o1.getStartTime() == null)
                    return -1;
                if (o2.getStartTime() == null)
                    return 1;
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        set.addAll(getTasksList());
        set.addAll(getSubtasksList());
        return new ArrayList<Task> (set);
    }

    public boolean isNotIntersection(Task task) {
        for (Task treeTask : getPrioritizedTasks()) {
            if (task.getStartTime().isAfter(treeTask.getStartTime())
                    && task.getStartTime().isBefore(treeTask.getEndTime())) {
                return false;
            }
            if (task.getEndTime().isAfter(treeTask.getStartTime())
                    && task.getEndTime().isBefore(treeTask.getEndTime())) {
                return false;
            }
            if (task.getStartTime().equals(treeTask.getStartTime())) {
                return false;
            }
            if (task.getEndTime().equals(treeTask.getStartTime())) {
                return false;
            }
        }
        return true;
    }
}
