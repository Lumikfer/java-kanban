package tasks;

import statuses.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
     protected final TaskType typeEpic = TaskType.EPIC;
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description, StatusTask status, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.subtasks = new ArrayList<>();
    }


    public Epic(String name, String description, StatusTask status, int id, LocalDateTime startTime) {
        super(name, description, status, id, startTime);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        if (checkNotContainsSubtask(subtask)) {
            subtasks.add(subtask.getId());
            subtask.setEpicId(this.id);
        } else {
            System.out.println("Данная подзадача уже существует в списке");
        }
    }

    public void removeSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void clearSubtaskList() {
        subtasks.clear();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", id, typeEpic, name, status, description, duration, startTime,
                endTime);
    }

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.contains(subtask.getId());
    }
}
