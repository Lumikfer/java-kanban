import java.util.*;


public class TaskManager {
    Scanner scanner = new Scanner(System.in);
    HashMap<Integer,Task> tasks = new HashMap<>();
    HashMap<Integer,SubTask> SubTasks = new HashMap<>();
    HashMap<Integer,Epic> Epics = new HashMap<>();
    int taskCount = 0;
    public void addTask(Task task) {
        task.setId(++taskCount);
        tasks.put(task.getId(), task);
    }
    public void addSubTask(SubTask subTask,Epic epic) {
        int id = ++taskCount;
        subTask.setId(id);
        Epic.setSubtaskID(id);
        SubTasks.put(subTask.getId(), subTask);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    public void addEpic(Epic Epic) {

        Epic.setId(++taskCount);
        Epics.put(Epic.getId(), Epic);
    }

    public void updateSubTask(SubTask SubTask) {
        SubTasks.put(SubTask.getId(), SubTask);
        
    }
   public void updateEpic(Epic Epic) {
        Epics.put(Epic.getId(), Epic);

   }
   public void printAll() {
        ArrayList<String> list = new ArrayList<>();
        System.out.println("Tasks:");
        if(tasks.isEmpty())
        {
            System.out.println("тут пусто");

        }
        else {

            System.out.println(tasks.values());
        }

   }
   public void printId(HashMap<Integer,Task> tasks) {
        System.out.println("ID?");
        ArrayList<Task> list = new ArrayList<>();
        int ID = scanner.nextInt();
        list.add(tasks.get(ID));
        System.out.println("Tasks   ");
        System.out.println(list);


   }
   public void deleteID(HashMap<Integer,Task> tasks) {
       System.out.println("ID?");
       int ID = scanner.nextInt();
        if(tasks.containsKey(ID))
        {
            tasks.remove(ID);
        }
        else {
            System.out.println("такого нет");
        }
   }
   public ArrayList<SubTask> printSubEpic(Epic epic) {
        System.out.println("Epic id:");
        int ID = scanner.nextInt();
       ArrayList<SubTask> subtasksNew = new ArrayList<>();
        if(Epics.containsKey(ID))
        {
            //Epic thisEpic = Epics.get(ID);
            for(int i =0;i<Epic.getSubtaskID().size();i++)
            {
                subtasksNew.add(SubTasks.get(Epic.getSubtaskID().get(i)));

            }
            return subtasksNew;
        }
        else {
            System.out.println("Пусто");
        }
        return subtasksNew;
   }
   public void updateStatusEpic(Status.StatusType status, Epic epic, SubTask subTask)
   {
       ArrayList<SubTask> subtasks = new ArrayList<>();
       int Done = 0;
       int New = 0;
       for(int i =0;i<Epic.getSubtaskID().size();i++)
       {
           subtasks.add(SubTasks.get(Epic.getSubtaskID().get(i)));
           for(SubTask sub:subtasks)
           {
               if(Status.StatusType.NEW.equals(sub.getStatus()))
               {
                   New++;
               }
               if(Status.StatusType.DONE.equals(sub.getStatus()))
               {
                   Done++;
               }
               else {
                   epic.setStatus(Status.StatusType.IN_PROGRESS);
               }
           }
           if(Done == epic.getSubtaskID().size())
           {
               epic.setStatus(Status.StatusType.DONE);
           }
           if(New == epic.getSubtaskID().size())
           {
               epic.setStatus(Status.StatusType.NEW);
           }
           else {
               epic.setStatus(Status.StatusType.IN_PROGRESS);
           }


       }

   }

}
