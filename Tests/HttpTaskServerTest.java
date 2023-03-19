import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rent.server.HttpTaskServer;
import rent.server.KVServer;
import rent.service.Managers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HttpTaskServerTest {

    private static KVServer kvServer;
    private static HttpTaskServer taskServer;
    private static Gson gson = Managers.getGson();
    private static final String TASK_URL = "http://localhost:8080/tasks/task/";
    private static final String EPIC_URL = "http://localhost:8080/tasks/epic/";
    private static final String SUBTASK_URL = "http://localhost:8080/tasks/subtask/";
    private static final String HISTORY_URL = "http://localhost:8080/tasks/history";
    private static final String PRIORITIZED_TASKS_URL = "http://localhost:8080/tasks/";
    private static final String EPIC_SUBTASKS_URL = "http://localhost:8080/tasks/subtask/epic/?id=";

    @BeforeAll
    static void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskServer = new HttpTaskServer();
            taskServer.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @AfterAll
    static void stopServer() {
        kvServer.stop();
        taskServer.stop();
    }

    @BeforeEach
    void beforeEach() {

    }
}
