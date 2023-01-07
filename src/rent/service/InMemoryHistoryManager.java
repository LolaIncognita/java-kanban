package rent.service;
import rent.model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    static ArrayList<Task> taskHistory = new ArrayList<>();
    Integer historyMaxSize = 10;

    @Override
    public ArrayList<Task> getHistory() {
        return taskHistory;
    }

    @Override
    public void add(Task task) {
        if (taskHistory.size() < historyMaxSize) {
            taskHistory.add(task);
        } else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }
}
