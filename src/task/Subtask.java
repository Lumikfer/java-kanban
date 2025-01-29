package task;

import status.Status;

public class Subtask extends Task {
    private int epicId;
    protected TaskEnum type = TaskEnum.SUBTASK;

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

    public static Subtask fromString(String value) {
        String[] str = value.split(",");

        return new Subtask(str[2], str[4], Integer.parseInt(str[0]), Status.valueOf(str[3]), Integer.parseInt(str[5]));
    }

    @Override
    public String toString() {
        String line = "" + id + "," + type + "," + name + "," + status + "," + description + "," + epicId;
        return line;
    }
}