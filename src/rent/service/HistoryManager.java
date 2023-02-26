package rent.service;
import rent.model.Task;

public interface HistoryManager {

    void addHistory(Task task);

    void removeHistory(int id);

     void getHistory();
}
