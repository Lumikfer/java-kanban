import java.util.*;


public class  TaskManager {
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

    public void updateSubTask(SubTask subTask,Epic epic) {
        SubTasks.put(subTask.getId(), subTask);
        int epid = subTask.getEpicId();
        Epic ep = Epics.get(epid);
        updateStatusEpic(ep,subTask);

    }
   public void updateEpic(Epic Epic) {
        Epics.put(Epic.getId(), Epic);

   }
   public ArrayList<Task> printAllTask() {
        System.out.println("Tasks:");
        if(tasks.isEmpty())
        {
            System.out.println("тут пусто");
        }
        else {

            System.out.println(tasks.values());
        }
        return  new ArrayList<>(tasks.values());

   }
   public void printId(HashMap<Integer,Task> tasks) {
        System.out.println("ID?");
        ArrayList<Task> list = new ArrayList<>();
        int ID = scanner.nextInt();
        list.add(tasks.get(ID));
        System.out.println("Tasks   ");
        System.out.println(list);


   }
   public void deleteIDTask(HashMap<Integer,Task> tasks) {
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
       ArrayList<SubTask> subtasksNew = new ArrayList<>();
        if(Epics.containsKey(epic.getId()))
        {
            //Epic thisEpic = Epics.get(ID);
            for(int i =0;i<Epic.getSubtaskID().size();i++)
            {
                subtasksNew.add(SubTasks.get(Epic.getSubtaskID().get(i)));
            }
            System.out.println(subtasksNew);
        }
        else {
            System.out.println("Пусто");
        }
        return subtasksNew;

   }
   public void updateStatusEpic( Epic epic, SubTask subTask)
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
   public ArrayList<Epic> getEpic()
   {
       return new ArrayList<>(Epics.values());
   }
   public ArrayList<SubTask> getSubtask()
   {
       return new ArrayList<>(SubTasks.values());
   }
   public ArrayList<SubTask> getSubById(int id)
   {
       ArrayList<SubTask> subs = new ArrayList<>();
       subs.add(SubTasks.get(id));
       return subs;
   }
   public ArrayList<Epic>  getEpicByID(int id)
   {
       ArrayList<Epic> epics = new ArrayList<>();
       epics.add(Epics.get(id));
       return epics;
   }
   public ArrayList<Task> getTaskByID(int id)
   {
       ArrayList<Task> tasksid = new ArrayList<>();
       tasksid.add(tasks.get(id));
       return tasksid;
   }
   public void deleteTaskById(int id)
   {
       tasks.remove(id);
   }
   public void deleteSubTaskById(int id)
   {
       SubTasks.remove(id);
   }
   public void deleteEpicById(int id)
   {
       Epics.remove(id);
   }
   public void  deleteAllTask()
   {
       tasks.clear();
   }
   public void deleteAllEpics()
   {
       Epics.clear();
   }
   public void deleteAllSubTask()
   {
       SubTasks.clear();
   }

}
