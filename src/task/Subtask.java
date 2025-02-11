package task;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    protected TaskEnum type = TaskEnum.SUBTASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Subtask(String name, String description, int id, Status status, int epicId,LocalDateTime startTime,Duration duration) {
        super(name, description, status, id,startTime,duration);
        this.epicId = epicId;
        this.endTime = startTime.plus(duration);
    }

    public Subtask(String name, String description, int epicId, Status status,LocalDateTime startTime,Duration duration) {
        super(name, description, status,startTime,duration);
        this.epicId = epicId;
        this.endTime = startTime.plus(duration);
    }
    public Subtask(String name, String description, int epicId,LocalDateTime startTime,Duration duration) {
        super(name, description,startTime,duration);
        this.epicId = epicId;
        this.status = Status.NEW;
        this.endTime = startTime.plus(duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String line = "" +id+","+type+"," + name+"," + status+"," + description+","+epicId;
        return line;
    }
}