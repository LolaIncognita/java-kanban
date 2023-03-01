package rent.service;
import rent.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> taskHistoryList = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return taskHistoryList.getTasks();
    }

    @Override
    public void addHistory(Task task) {
        taskHistoryList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        taskHistoryList.removeNode(taskHistoryList.historyMap.get(id));
        taskHistoryList.removeInMapForHistory(id);
    }

    class CustomLinkedList<T extends Task> {
        protected Node<T> head;
        protected Node<T> tail;
        protected int size = 0;
        HashMap<Integer, Node<T>> historyMap = new HashMap<>();

        public void linkLast(T task) {
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.get(task.getId()));
                removeInMapForHistory(task.getId());
            }
            Node<T> oldTail = null;
            if(tail != null) {
                oldTail = this.tail;
            }
            Node<T> newNode = new Node<>(oldTail, task, null);
            historyMap.put(task.getId(), newNode);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }

        public ArrayList<Task> getTasks () {
            ArrayList<Task> tasksHistory = new ArrayList<>();
            Node node = this.tail;
            while (node != null) {
                tasksHistory.add(node.data);
                node = node.prev;
            }
            return tasksHistory;
        }

        public void removeInMapForHistory(Integer id) {
            if(historyMap.containsKey(id)) {
                historyMap.remove(id);
            }
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
                System.out.println("Задача не была просмотрена ранее");
            } else {
                Node<T> prevTask = null;
                Node<T> nextTask = null;

                if(node.prev != null && node.next != null) {
                    prevTask = node.prev;
                    nextTask = node.next;
                    prevTask.next = nextTask;
                    nextTask.prev = prevTask;
                } else if (node.prev != null) {
                    prevTask = node.prev;
                    prevTask.next = null;
                    tail = prevTask;
                } else if (node.next != null) {
                    nextTask = node.next;
                    nextTask.prev = null;
                    head = nextTask;
                }
                node.prev = null;
                node.next = null;
                size--;
            }
        }
    }
}
