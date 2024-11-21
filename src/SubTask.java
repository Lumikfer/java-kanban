public class SubTask extends Task {
    private  int epicId;

    public SubTask(String name, String description, Status.StatusType status, int epicId) {
        super(name,description,status);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }


    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() +
                ", description='" + getDescriotion() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                '}';
    }



}
