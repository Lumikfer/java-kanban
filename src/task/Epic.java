package task;

import status.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    protected TaskEnum type = TaskEnum.EPIC;

    public Epic(String name, String description, Status status, int id) {
        super(name, description, Status.NEW);

    }

    public Epic(String name, String description, int id) {
        super(name, description, Status.NEW, id);
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subtasksId.add(subtaskId);
        }
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    @Override
    public String toString() {
        String line = "" + id + "," + type + "," + name + "," + status + "," + description;
        return line;
    }

    public void removeSubtaskById(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

}