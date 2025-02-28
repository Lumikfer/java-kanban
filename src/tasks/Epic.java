package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, Duration.ZERO, null);
    }

    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status, Duration.ZERO, null);
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateEpicFields();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
        updateEpicFields();
    }

    public void clearSubtasks() {
        subtaskList.clear();
        updateEpicFields();
    }

    private void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void updateEpicFields() {
        if (subtaskList.isEmpty()) {
            setDuration(Duration.ZERO);
            setStartTime(null);
            setEndTime(null);
            return;
        }

        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtaskList) {
            if (subtask.getStartTime() != null) {
                if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subtask.getStartTime();
                }
            }
            if (subtask.getEndTime() != null) {
                if (latestEndTime == null || subtask.getEndTime().isAfter(latestEndTime)) {
                    latestEndTime = subtask.getEndTime();
                }
            }
            totalDuration = totalDuration.plus(subtask.getDuration());
        }

        setDuration(totalDuration);
        setStartTime(earliestStartTime);
        setEndTime(latestEndTime);
    }

    @Override
    public String toString() {
        return "Epic{" + "name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", subtaskList.size=" + subtaskList.size() + ", status=" + getStatus() + ", duration=" + getDuration().toMinutes() + " minutes" + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + "}";
    }
}
