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
    String[] status = {"NEW", "IN_PROGRESS", "DONE"};



    public Manager() {
    }
    public Task makeNewTask(Task task, String description) {
        countOfTasks++;
        task.uniqueIdentificationNumber = countOfTasks;
        task.taskDescription = description;
        task.setTaskStatus("NEW");
        allTasksMap.put(task.uniqueIdentificationNumber, task);
        addTask(task);
        return task;
    }

    public Epic makeNewEpic(Epic epic, String description) {
        countOfTasks++;
        epic.uniqueIdentificationNumber = countOfTasks;
        epic.taskDescription = description;
        epic.setEpicStatus("NEW");
        allTasksMap.put(epic.uniqueIdentificationNumber, epic);
        addEpic(epic);
        return epic;
    }

    public Subtask makeNewSubtask(Subtask subtask, Epic epic, String description) {
        countOfTasks++;
        subtask.uniqueIdentificationNumber = countOfTasks;
        subtask.epicID = epic.uniqueIdentificationNumber;
        subtask.taskDescription = description;
        subtask.setSubtaskStatus("NEW");
        epic.joinSubtaskToEpic(epic, subtask);
        allTasksMap.put(subtask.uniqueIdentificationNumber, subtask);
        addSubtask(subtask);
        return subtask;
    }


    public void addTask(Task task) {
        taskHashMap.put(task.uniqueIdentificationNumber, task);
    }
    public void addEpic(Epic epic) {
        epicsHashMap.put(epic.uniqueIdentificationNumber, epic);
    }
    public void addSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.uniqueIdentificationNumber, subtask);
    }
    public HashMap<Integer, Task> getTasksList() {
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

    public Task getTaskByIdentifyCod(Integer identifyCod) {
        Task task = taskHashMap.get(identifyCod);
        return task;
    }

    public Epic getEpicByIdentifyCod(Integer identifyCod) {
        Epic epic = epicsHashMap.get(identifyCod);
        return epic;
    }
    public Subtask getSubtaskByIdentifyCod(Integer identifyCod) {
        Subtask subtask = subtaskHashMap.get(identifyCod);
        return subtask;
    }

    public void removeTaskByIdentifyCod(Integer identifyCod) {
        taskHashMap.remove(identifyCod);
        allTasksMap.remove(identifyCod);
        System.out.println("Задача с уникальным идентификационным номером "
                + identifyCod + " удалена.");
    }
    public void removeEpicByIdentifyCod(Integer identifyCod) {
        epicsHashMap.remove(identifyCod);
        allTasksMap.remove(identifyCod);
        System.out.println("Эпик с уникальным идентификационным номером "
                + identifyCod + " удалён.");
    }
    public void removeSubtaskByIdentifyCod(Integer identifyCod) {
        subtaskHashMap.remove(identifyCod);
        allTasksMap.remove(identifyCod);
        System.out.println("Подзадача с уникальным идентификационным номером "
                + identifyCod + " удалена.");
    }
   public HashMap<Integer, Task> getAllTypeTasksMap() {
        return allTasksMap;
    }

   public ArrayList<Integer> getSubtaskOfEpic(Epic epic) {
        ArrayList<Integer> subtasksOfEpic = epic.getSubtasksOfEpic(epic);
        return subtasksOfEpic;
   }

    public void changeSubtaskStatus(Subtask subtask, String newStatus) {
        subtask.setSubtaskStatus(newStatus);
        Epic epic = getEpicByIdentifyCod(subtask.epicID);
        ArrayList<String> subtaskStatusOfEpic = new ArrayList<>();
        ArrayList<Integer> subtaskID = epic.getSubtasksOfEpic(epic);

        for(int i = 0; i < subtaskID.size(); i++) {
            Subtask otherSubtask = getSubtaskByIdentifyCod(subtaskID.get(i));
            subtaskStatusOfEpic.add(otherSubtask.getSubtaskStatus());
            }
        if(subtaskStatusOfEpic.contains("NEW") && subtaskStatusOfEpic.contains("DONE")) {
            epic.setEpicStatus("IN_PROGRESS");
        } else if (!subtaskStatusOfEpic.contains("NEW")) {
            epic.setEpicStatus("DONE");
        }
    }

    public void updateTask(Integer taskID, Task newTask) {
        newTask.uniqueIdentificationNumber = taskID;
        Task task = getTaskByIdentifyCod(taskID);
        newTask.setTaskStatus(task.getTaskStatus());
        removeTaskByIdentifyCod(taskID);
        allTasksMap.put(newTask.uniqueIdentificationNumber, newTask);
        taskHashMap.put(newTask.uniqueIdentificationNumber, newTask);
    }
    public void updataEpic(Integer epicID, Epic newEpic) {
        newEpic.uniqueIdentificationNumber = epicID;
        Epic epic = getEpicByIdentifyCod(epicID);
        newEpic.setEpicStatus(epic.getEpicStatus());
        removeEpicByIdentifyCod(epicID);
        allTasksMap.put(newEpic.uniqueIdentificationNumber, newEpic);
        epicsHashMap.put(newEpic.uniqueIdentificationNumber, newEpic);
    }
    public void updateSubtask(Integer subtaskID, Subtask newSubtask) {
        newSubtask.uniqueIdentificationNumber = subtaskID;
        Subtask subtask = getSubtaskByIdentifyCod(subtaskID);
        newSubtask.setSubtaskStatus(subtask.getTaskStatus());
        newSubtask.epicID = subtask.epicID;
        removeSubtaskByIdentifyCod(subtaskID);
        allTasksMap.put(newSubtask.uniqueIdentificationNumber, newSubtask);
        subtaskHashMap.put(newSubtask.uniqueIdentificationNumber, newSubtask);
    }
}

