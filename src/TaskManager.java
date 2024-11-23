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
        subTask.setEpicId(epic.getId());
        SubTasks.put(subTask.getId(), subTask);
        updateStatusEpic(epic);
    }

    public void updateTask(Task task, Status.StatusType status) {
        tasks.put(task.getId(), task);
        task.setStatus(status);
    }
    public void addEpic(Epic Epic) {

        Epic.setId(++taskCount);
        Epics.put(Epic.getId(), Epic);
    }


   public void updateEpic(Epic epic) {
        Epics.put(epic.getId(), epic);


   }
   public void printAllTask() {
        System.out.println("Tasks:");
        if(tasks.isEmpty())
        {
            System.out.println("тут пусто");
        }
        else {
            System.out.println(tasks.values());

        }

   }
   public void printTaskId(HashMap<Integer,Task> tasks) {
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
   public void updateStatusEpic(Epic epic) {
       ArrayList<SubTask> subtasks = new ArrayList<>();
       int Done = 0;
       int New = 0;
       if(Epic.getSubtaskID().isEmpty())
       {
           epic.setStatus(Status.StatusType.NEW);
       }
           for (int j : Epic.getSubtaskID()) {
               subtasks.add(SubTasks.get(j));
           }
           for (SubTask task : subtasks) {
               if (Status.StatusType.NEW.equals(task.getStatus())) {
                   ++New;
               } else if (Status.StatusType.DONE.equals(task.getStatus())) {
                   ++Done;
               }
           }
                for(int i =0;i<Epic.getSubtaskID().size();i++) {
                    if (Epic.getSubtaskID() != null) {
                        if (Epic.getSubtaskID().size() == Done) {
                            epic.setStatus(Status.StatusType.DONE);
                        } else if (Epic.getSubtaskID().size() == New) {
                            epic.setStatus(Status.StatusType.NEW);

                        } else {
                            epic.setStatus(Status.StatusType.IN_PROGRESS);
                        }
                    }
                }




   }
   public void getEpic()
   {
       System.out.println(Epics.values());
   }
   public void getSubtask()
   {
       System.out.println(SubTasks.values());
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

       SubTask subTask = SubTasks.get(id);
       Epic epic = Epics.get(subTask.getEpicId());

       epic.getSubtaskID().remove((Integer) subTask.getId());
       updateStatusEpic(epic);

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
       SubTasks.clear();
   }
   public void deleteAllSubTask()
   {
       SubTasks.clear();
       for(Epic epic: Epics.values())
       {
           epic.getSubtaskID().clear();
           updateStatusEpic(epic);
       }


   }
    public void updateSubTask(SubTask subTask,Epic epic,Status.StatusType status) {
        SubTasks.put(subTask.getId(), subTask);
        subTask.setStatus(status);
        updateStatusEpic(epic);

    }


}
