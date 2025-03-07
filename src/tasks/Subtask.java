package tasks;

import statuses.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final TaskType typeSubTask = TaskType.SUBTASK;
    private int epicId;

    public Subtask(String name, String description, StatusTask status, int epicId, int id, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId, int id, LocalDateTime startTime) {
        super(name, description, status, id, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%s", id, typeSubTask, name, status, description, epicId, duration,
                startTime, endTime);
    }
}
