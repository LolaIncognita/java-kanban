package rent.service;

import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManagers extends InMemoryTaskManager {
    File file;
    HistoryManager historyManager;

    public FileBackedTasksManagers(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        this.historyManager = historyManager;
    }


    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        File file = new File("src/rent/resources", "historyFile");
        FileBackedTasksManagers fileBackedTasksManagers = new FileBackedTasksManagers(historyManager, file);
    }

    //должен сохранять текущее состояние менеджера в указанный файл - т.е. все задачи, подзадачи,
    // эпики и историю просмотра любых задач. Для удобства работы лучше выбрать текстовый файл CSV
    public void save () throws ManagersSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic,startTime,duration\n");
            for (Task task: tasksList) {
                fileWriter.write(toString(task));
            }
            for (Epic epic: epicsList) {
                fileWriter.write(toString(epic));
            }
            for (Subtask subtask: subtasksList) {
                fileWriter.write(toString(subtask));
            }
            if (historyToString(historyManager) != null) {
                fileWriter.write("\n");
                fileWriter.write(historyToString(historyManager));
            }
        } catch (IOException e) {
            throw new ManagersSaveException("Произошла ошибка во время записи файла.");
        }
    }

    //должен восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTasksManagers loadFromFile(File file) {
        HistoryManager recoveredHistoryManager = new InMemoryHistoryManager();
        FileBackedTasksManagers fileBackedTasksManager = new FileBackedTasksManagers(recoveredHistoryManager, file);
        List<String> lines = List.of(fileBackedTasksManager.readFile(file).split("\n"));
        List<Integer> tasksIdForHistory;
        if (lines.size() > 3) {
            for (int i = 1; i < lines.size(); i++) {
                if (!lines.get(i).isEmpty()) {
                    if(!lines.get(i-1).isEmpty()) {
                        fileBackedTasksManager.fromString(lines.get(i));
                    } else {
                        tasksIdForHistory = historyFromString(lines.get(i));
                        for (int g = 0; g < tasksIdForHistory.size(); g++) {
                            recoveredHistoryManager.addHistory(fileBackedTasksManager.allTasksMap.get(tasksIdForHistory.get(tasksIdForHistory.size() - 1 - g)));
                        }
                    }
                }
            }
        }
        return fileBackedTasksManager;
    }

    public String readFile(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка во время чтения файла");
        }
    }

    //метод сохранения задачи в строку - либо нужно переопределить базовый метод
    public String toString(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getClass().getSimpleName().toUpperCase(),
                task.getTaskName(), task.getTaskStatus(), task.getTaskDescription(), task.getStartTime(), task.getDuration());
    }

    public String toString(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", subtask.getId(),
                subtask.getClass().getSimpleName().toUpperCase(), subtask.getTaskName(), subtask.getTaskStatus(),
                subtask.getTaskDescription(), subtask.getEpicId(), subtask.getStartTime(), subtask.getDuration());
    }

     public Task fromString (String value) {
        //метод создания задачи из строки
         String[] values = value.split(",");
         Integer id = Integer.parseInt(values[0]);
         TypeOfTasks typeOfTasks = TypeOfTasks.valueOf(values[1]);
         String name = values[2];
         StatusOfTasks status = StatusOfTasks.valueOf(values[3]);
         String description = values[4];
         Integer epicOfSubtaskId = null;
         LocalDateTime startTime = null;
         Long duration = 0L;
         if (values.length > 7) {
             epicOfSubtaskId = Integer.parseInt(values[5]);
             if (values[6] != null) {
                 startTime = LocalDateTime.parse(values[6]);
             }
             duration = Long.parseLong(values[7]);
         } else {
             if (values[5] != null) {
                 startTime = LocalDateTime.parse(values[5]);
             }
             duration = Long.parseLong(values[6]);
         }


         switch (typeOfTasks) {
             case TASK:
                 Task task = new Task();
                 task.setId(id);
                 task.setTaskName(name);
                 task.setTaskDescription(description);
                 task.setTaskStatus(status);
                 task.setStartTime(startTime);
                 task.setDuration(duration);
                 allTasksMap.put(task.getId(), task);
                 tasksList.add(task);
                 return task;
             case EPIC:
                 Epic epic = new Epic();
                 epic.setId(id);
                 epic.setTaskName(name);
                 epic.setTaskDescription(description);
                 epic.setTaskStatus(status);
                 epic.setStartTime(startTime);
                 epic.setDuration(duration);
                 allTasksMap.put(epic.getId(), epic);
                 epicsList.add(epic);
                 return epic;
             case SUBTASK:
                 Subtask subtask = new Subtask();
                 subtask.setId(id);
                 subtask.setTaskName(name);
                 subtask.setTaskDescription(description);
                 subtask.setTaskStatus(status);
                 subtask.setEpicId(epicOfSubtaskId);
                 subtask.setStartTime(startTime);
                 subtask.setDuration(duration);
                 allTasksMap.put(subtask.getId(), subtask);
                 subtasksList.add(subtask);
                 return subtask;
         }
         return null;
    }

    public static String historyToString (HistoryManager manager) {
        //для сохранения менеджера истории в CSV
        List<Task> historyToString = manager.getHistory();
        StringBuilder builder = new StringBuilder();
        for(Task task: historyToString) {
            String taskId = Integer.toString(task.getId());
            builder.append(taskId);
            builder.append(",");
        }
        if(builder.length() > 0) {
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }

    public static List<Integer> historyFromString(String value) {
        //для восстановления менеджера истории из CSV
        List<Integer> historyFromString = new ArrayList<>();
        if (value.length() > 0) {
            try {
                String[] values = value.split(",");
                for(String part: values) {
                    historyFromString.add(Integer.parseInt(part));
                }
            } catch (NumberFormatException e) {
                System.out.println("Произошла ошибка во время чтения истории из файла.");
            }
        }
        return historyFromString;
    }

    @Override
    public Task makeNewTask(Task task) {
        Task newTask = super.makeNewTask(task);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return newTask;
    }

    @Override
    public Epic makeNewEpic(Epic epic) {
        Epic newEpic = super.makeNewEpic(epic);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return newEpic;
    }

    @Override
    public Subtask makeNewSubtask(Subtask subtask, Epic epic) {
        Subtask newSubtask = super.makeNewSubtask(subtask, epic);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return newSubtask;
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
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearEpicsList() {
        epicsList.clear();
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearSubtasksList() {
        subtasksList.clear();
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        Task taskById = super.getTaskById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return taskById;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = super.getEpicById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return epicById;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtaskById = super.getSubtaskById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
        return subtaskById;
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
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
        super.changeSubtaskStatus(subtask, newStatus);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Integer taskId, Task newTask, String newTaskName, String newTaskDescription) {
        super.updateTask(taskId, newTask, newTaskName, newTaskDescription);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Integer epicId, Epic newEpic) {
        super.updateEpic(epicId, newEpic);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Integer subtaskId, Subtask newSubtask) {
        super.updateSubtask(subtaskId, newSubtask);
        try {
            save();
        } catch (ManagersSaveException e) {
            System.out.println(e.getMessage());
        }
    }
}
