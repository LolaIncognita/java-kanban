package rent.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rent.adapter.LocalDateTimeAdapter;
import rent.service.historyManager.HistoryManager;
import rent.service.historyManager.InMemoryHistoryManager;

import java.io.File;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefaultForServer() {
        return new HttpTaskManager("http://localhost:");
    }

    public static TaskManager getDefault (HistoryManager historyManager) {
        return  new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory () {
        return new InMemoryHistoryManager();
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }
}