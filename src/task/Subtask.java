package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Epic epicTask;
    protected int epicId;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.status = Status.NEW;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        //epicTask.updateStatus();
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "\towned: EpicTask Id#" + epicTask.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask item = (Subtask) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }
}
