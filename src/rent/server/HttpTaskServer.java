package rent.server;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import rent.model.Epic;
import rent.model.Subtask;
import rent.model.Task;
import rent.service.Managers;
import rent.service.TaskManager;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer() throws IOException, InterruptedIOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress("localhost", PORT), 0);
        System.out.println(server);
        taskManager = Managers.getDefaultForServer();
        gson = Managers.getGson();
        server.createContext("/tasks/task", new TasksHandler());
        server.createContext("/tasks/subtask", new SubtasksHandler());
        server.createContext("/tasks/epic", new EpicsHandler());
        server.createContext("/tasks/", new AllTasksHandler());
        server.createContext("/tasks/history", new HistoryHandler());
        server.start();
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String query = httpExchange.getRequestURI().getQuery();
                String requestMethod = httpExchange.getRequestMethod();
                String response = null;
                switch (requestMethod) {
                    case "GET":
                        if (query == null) {
                            response = gson.toJson(taskManager.getTasksList());
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                response = gson.toJson(taskManager.getTaskById(id));
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    case "POST":
                        Task task = gson.fromJson(readText(httpExchange), Task.class);
                        if (taskManager.getTasksList().contains(task)) {
                            taskManager.updateTask(task.getId(), task, task.getTaskName(), task.getTaskDescription());
                            System.out.println("Задача успешно обновлена.");
                            writeResponse(httpExchange, response, 200);
                        } else {
                            taskManager.makeNewTask(task);
                            response = task.toString();
                            System.out.println("Задача успешно добавлена.");
                            writeResponse(httpExchange, response, 200);
                        }
                        break;
                    case "DELETE":
                        if (query == null) {
                            taskManager.clearTasksList();
                            System.out.println("Все задачи удалены.");
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                taskManager.removeTaskById(id);
                                System.out.println("Задача с id = " + id + "успешно удалена.");
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    default:
                        System.out.println("Такой метод не предусмотрен.");
                        writeResponse(httpExchange, response, 405);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

        protected String readText(HttpExchange httpExchange) throws IOException {
            return new String(httpExchange.getRequestBody().readAllBytes(),DEFAULT_CHARSET);
        }

        protected int parseIntID(String path) {
            try {
                return Integer.parseInt(path);
            } catch (NumberFormatException exception) {
                return -1;
            }
        }

        protected void writeResponse(HttpExchange httpExchange, String responseMessage, int responseCode) throws
                IOException {
            if (responseMessage.isBlank()) {
                httpExchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseMessage.getBytes(DEFAULT_CHARSET);
                httpExchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            httpExchange.close();
        }
    }

    class EpicsHandler extends TasksHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String query = httpExchange.getRequestURI().getQuery();
                String requestMethod = httpExchange.getRequestMethod();
                String response = null;
                switch (requestMethod) {
                    case "GET":
                        if (query == null) {
                            response = gson.toJson(taskManager.getEpicsList());
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                response = gson.toJson(taskManager.getEpicById(id));
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    case "POST":
                        Epic epic = gson.fromJson(readText(httpExchange), Epic.class);
                        if (taskManager.getEpicsList().contains(epic)) {
                            taskManager.updateEpic(epic.getId(), epic);
                            System.out.println("Эпик успешно обновлён.");
                            writeResponse(httpExchange, response, 200);
                        } else {
                            taskManager.makeNewEpic(epic);
                            response = epic.toString();
                            System.out.println("Эпик успешно добавлен.");
                            writeResponse(httpExchange, response, 200);
                        }
                        break;
                    case "DELETE":
                        if (query == null) {
                            taskManager.clearEpicsList();
                            System.out.println("Все эпики удалены.");
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                taskManager.removeEpicById(id);
                                System.out.println("Эпик с id = " + id + "успешно удалён.");
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    default:
                        System.out.println("Такой метод не предусмотрен.");
                        writeResponse(httpExchange, response, 405);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    class SubtasksHandler extends TasksHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String query = httpExchange.getRequestURI().getQuery();
                String requestMethod = httpExchange.getRequestMethod();
                String response = null;
                switch (requestMethod) {
                    case "GET":
                        if (query == null) {
                            response = gson.toJson(taskManager.getSubtasksList());
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                response = gson.toJson(taskManager.getSubtaskById(id));
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    case "POST":
                        Subtask subtask = gson.fromJson(readText(httpExchange), Subtask.class);
                        if (taskManager.getSubtasksList().contains(subtask)) {
                            taskManager.updateSubtask(subtask.getId(), subtask);
                            System.out.println("Сабтаска успешно обновлена.");
                            writeResponse(httpExchange, response, 200);
                        } else {
                            taskManager.makeNewSubtask(subtask, taskManager.getEpicById(subtask.getEpicId()));
                            response = subtask.toString();
                            System.out.println("Сабтаска успешно добавлена.");
                            writeResponse(httpExchange, response, 200);
                        }
                        break;
                    case "DELETE":
                        if (query == null) {
                            taskManager.clearSubtasksList();
                            System.out.println("Все сабтаски удалены.");
                            writeResponse(httpExchange, response, 200);
                            break;
                        }
                        if (Pattern.matches("^id=\\d+$", query)) {
                            String pathId = query.replaceFirst("id=", "");
                            int id = parseIntID(pathId);
                            if (id != -1) {
                                taskManager.removeSubtaskById(id);
                                System.out.println("Сабтаска с id = " + id + "успешно удалена.");
                                writeResponse(httpExchange, response, 200);
                            } else {
                                System.out.println("Получен некорректный id: " + pathId);
                                writeResponse(httpExchange, response, 405);
                            }
                        }
                        break;
                    default:
                        System.out.println("Такой метод не предусмотрен.");
                        writeResponse(httpExchange, response, 405);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                String response = null;
                if("GET".equals(requestMethod)) {
                    response = gson.toJson(taskManager.getHistoryManager());
                    writeResponse(httpExchange, response, 200);
                } else {
                    System.out.println("Предусмотрены только GET запросы, метод + " + requestMethod + " не обрабатывается.");
                    writeResponse(httpExchange, response, 405);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

        protected void writeResponse(HttpExchange httpExchange, String responseMessage, int responseCode) throws
                IOException {
            if (responseMessage.isBlank()) {
                httpExchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseMessage.getBytes(DEFAULT_CHARSET);
                httpExchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            httpExchange.close();
        }
    }

    class AllTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                String response = null;
                if ("GET".equals(requestMethod)) {
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    writeResponse(httpExchange, response, 200);
                } else {
                    System.out.println("Предусмотрены только GET запросы, метод + " + requestMethod + " не обрабатывается.");
                    writeResponse(httpExchange, response, 405);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

        protected void writeResponse(HttpExchange httpExchange, String responseMessage, int responseCode) throws
                IOException {
            if (responseMessage.isBlank()) {
                httpExchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseMessage.getBytes(DEFAULT_CHARSET);
                httpExchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " остановлен.");
        server.stop(0);
    }
}


