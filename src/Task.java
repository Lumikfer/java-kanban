import java.util.Objects;

public class Task {
    private  String name;
    private String descriotion;
    private int id;
    private Status.StatusType status;
    public Task(String name, String descriotion, Status.StatusType status, int id) {
        this.name = name;
        this.descriotion = descriotion;
        this.id = id;

    }

    public Task(String description, String name, Status.StatusType status) {
        this.name = name;
        this.descriotion = description;
        this.status = status;

    }

    public void setId(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return id;
    }
    public void setDescriotion(String descriotion)
    {
        this.descriotion = descriotion;
    }
    public String getDescriotion()
    {
        return descriotion;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public void setStatus(Status.StatusType status){this.status = status;}
    public Status.StatusType getStatus()
    {
        return status;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(descriotion, task.descriotion) && Objects.equals(name, task.name)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriotion, id, name, status);
    }
    public String toString() {
        return "Task{" +
                "description='" + descriotion + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

}
