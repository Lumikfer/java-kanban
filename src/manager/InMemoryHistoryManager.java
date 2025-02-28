package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final MyLinkedList historiesList = new MyLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            historiesList.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historiesList.getTasks();
    }

    @Override
    public void remove(int id) {
        historiesList.removeNode(historiesList.getNode(id));
    }

    private static class MyLinkedList {
        private final Map<Integer, Node> nodeMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            if (nodeMap.containsKey(task.getId())) {
                removeNode(nodeMap.get(task.getId()));
            }

            Node element = new Node();
            element.setTask(task);

            if (head == null) {
                head = element;
            } else {
                element.setPrev(tail);
                tail.setNext(element);
            }
            tail = element;

            nodeMap.put(task.getId(), element);
        }

        private List<Task> getTasks() {
            List<Task> taskList = new ArrayList<>();
            Node element = head;
            while (element != null) {
                taskList.add(element.getTask());
                element = element.getNext();
            }
            return taskList;
        }

        private void removeNode(Node node) {
            if (node != null) {
                nodeMap.remove(node.getTask().getId());
                Node prev = node.getPrev();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrev();
                }
                if (prev != null) {
                    prev.setNext(next);
                }
                if (next != null) {
                    next.setPrev(prev);
                }
            }
        }

        private Node getNode(int id) {
            return nodeMap.get(id);
        }

    }

    static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

}