package managers;

import Task.Task;

import java.util.ArrayList;
public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();
    private  int historySize =0;
    private Node head;
    private Node tail;





    @Override
    public boolean add(Task task) {
        if (task == null) return false;
        if (history.containsKey(task.getId()))  remove(task.getId());
        linkLast(task);
        if (historySize > 0 && history.size() > historySize)  remove(head.task.getId());
        return true;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node curNode = head;
        while (curNode != null) {
            historyList.add(curNode.task);
            curNode = curNode.next;
        }
        return historyList;
    }

    @Override
    public boolean remove(int id) {
        Node nodeToRemove = history.get(id);
        if (nodeToRemove == null) return false;
        removeNode(nodeToRemove);
        history.remove(id);
        return true;
    }

    private boolean linkLast(Task task) {
        if (task == null) return false;
        if (tail == null) {
            head = new Node(null, task, null);
            tail = head;
        } else {
            tail.next = new Node(tail, task, null);
            tail = tail.next;
        }
        return true;
    }

    private boolean removeNode(Node node) {
        if (node == null) return false;
        if (node == head && head == tail) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = head.next;
            head.prev = null;
        } else if (node == tail) {
            tail = tail.prev;
            tail.next = null;
        } else if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else {
            return false;
        }
        return true;
    }

    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }
}