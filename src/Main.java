
public class Main {

    public static void main(String[] args) {
        Task firstTask = new Task("Разработать лифт до луны", "Космолифт", Status.StatusType.NEW);
        Epic firstEpic = new Epic("Посадить дерево", "Дерево", Status.StatusType.NEW);
        SubTask firstSubtask = new SubTask("Купить семена", "Семена", Status.StatusType.NEW, firstEpic.getId());
        SubTask firstSubtask1 = new SubTask("Купить молоко", "хлеб", Status.StatusType.NEW, firstEpic.getId());
        SubTask firstSubtask2 = new SubTask("Купить семена333", "Семена12", Status.StatusType.NEW, firstEpic.getId());
        Epic epic2 =new Epic("Посадить дерево", "Дерево", Status.StatusType.NEW);

        TaskManager manager = new TaskManager();
        manager.addTask(firstTask);
        manager.addEpic(firstEpic);
        manager.addSubTask(firstSubtask,firstEpic);
        manager.addSubTask(firstSubtask1,firstEpic);
        manager.updateSubTask(firstSubtask,firstEpic, Status.StatusType.DONE);
        manager.updateSubTask(firstSubtask1,epic2, Status.StatusType.DONE);
        manager.addSubTask(firstSubtask2,firstEpic);
        //System.out.println(manager.getSubById(5));

        //manager.getSubtask();

        manager.getEpic();
        //manager.deleteSubTaskById(3);
        manager.getEpic();
        manager.getSubtask();














    }
}
