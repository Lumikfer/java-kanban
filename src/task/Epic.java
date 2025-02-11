package task;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    protected TaskEnum type = TaskEnum.EPIC;
    protected Duration duration;
    protected LocalDateTime endTime;
    protected LocalDateTime startTime;

    public Epic(String name, String description, Status status, int id, LocalDateTime startTime, Duration duration) {
        super(name, description, Status.NEW,startTime,duration);
        
    }

    public Epic(String name, String description, int id,LocalDateTime startTime,Duration duration) {
        super(name, description, Status.NEW, id,startTime,duration);
    }

    public Epic(String name, String description,LocalDateTime startTime,Duration duration) {
        super(name, description, Status.NEW,startTime,duration);
    }

    public Epic(String name, String description,int id) {
        super(name,description,id);
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

    public void removeSubtaskById(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime =startTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public  Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        long durat = duration.toMinutes();
        String local = startTime.format(DATE_TIME_FORMATTER);
        String localend = endTime.format(DATE_TIME_FORMATTER);
        String line = id+","+type+"," + name+"," + status+"," + description+","+duration+","+local+","+localend ;
        return line;
    }
}