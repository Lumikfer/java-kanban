package task;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskEnum type = TaskEnum.TASK;
    protected Duration duration;
    protected LocalDateTime endTime;
    protected LocalDateTime startTime;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public Task(String name, String description, Status status,LocalDateTime startTime,Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description,LocalDateTime startTime,Duration duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, Status status,int id, LocalDateTime startTime,Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration.toMinutes());
        this.endTime = startTime.plus(duration);
    }
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public  Duration getDuration(){
        return duration;
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        long durat = duration.toMinutes();
        String local = startTime.format(DATE_TIME_FORMATTER);
        String localend = endTime.format(DATE_TIME_FORMATTER);
        String line = id+","+type+"," + name+"," + status+"," + description+","+duration+","+local+","+localend ;
        return line;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}