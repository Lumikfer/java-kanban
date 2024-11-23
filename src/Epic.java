import java.util.ArrayList;

public class Epic extends Task{
   private static ArrayList<Integer> subtaskID ;
    public Epic(String description, String name, Status.StatusType status) {
        super(description, name, status);
        subtaskID = new ArrayList<>();
    }
    public static void setSubtaskID(int id){
        subtaskID.add(id);
    }
    public static ArrayList<Integer> getSubtaskID(){
        return subtaskID;
    }
    public void delsub(int id)
    {
        subtaskID.remove(id);
    }
    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskID +
                ", description='" + getDescriotion() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
