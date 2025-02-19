package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(long id, String name, String description, long duration, String startTime) {
        super(id, name, description, duration, startTime);
        this.taskType = TaskType.EPIC;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic item = (Epic) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void removeSubtaskById(Long id) {
        subtasksId.remove(Long.valueOf(id));
    }


}
