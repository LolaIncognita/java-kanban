package rent.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import rent.client.KVTaskClient;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.historyManager.HistoryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static rent.service.Managers.getDefaultHistory;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final int PORT = 8078;
    private final String url;
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager (String url) {
        super(getDefaultHistory (), new File("src/rent/resources", "historyFile"));
        this.url = url + PORT;
        gson = Managers.getGson();
        client = new KVTaskClient(this.url);
    }

    public void load() {
        List<Task> tasks = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        System.out.println(tasks);

        if (tasks != null) {
            for (Task task : tasks) {
                makeNewTask(task);
            }
        }

        List<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        if (epics != null) {
            for (Epic epic : epics) {
                makeNewEpic(epic);
            }
        }

        List<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {

        }.getType());

        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                makeNewSubtask(subtask, getEpicById(subtask.getEpicId()));
            }
        }
        List<Task> history = gson.fromJson(client.load("history"), new TypeToken<List<Task>>() {
        }.getType());

        if (history != null) {
            for (Task task : history) {
                getHistoryManager().addHistory(task);
            }
        }
    }

    @Override
    public void save() {
        System.out.println("HttpTaskManager save");
        client.put("tasks", gson.toJson(getTasksList()));
        client.put("epics", gson.toJson(getEpicsList()));
        client.put("subtasks", gson.toJson(getSubtasksList()));
        client.put("history", gson.toJson(getAllTypeTasksIdList()));
    }
}
