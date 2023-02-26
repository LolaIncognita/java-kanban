package rent.service;
import rent.model.Task;
import java.util.Objects;

public class Node <T extends Task> {
    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Node otherNode = (Node) obj;
        return Objects.equals(otherNode.data, this.data) && Objects.equals(otherNode.prev, this.prev) &&
                Objects.equals(otherNode.next, this.next);
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeData = '" + data + '\'' +
                ", nodePrev = '" + prev + '\'' +
                ", nodeNext = '" + next + '\'' +
                '}';
    }
}
