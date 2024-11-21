
public class Main {

    public static void main(String[] args) {
        Task firstTask = new Task("Разработать лифт до луны", "Космолифт", Status.StatusType.NEW);
        Epic firstEpic = new Epic("Посадить дерево", "Дерево", Status.StatusType.NEW);
        SubTask firstSubtask = new SubTask("Купить семена", "Семена", Status.StatusType.NEW, firstEpic.getId());
        SubTask firstSubtask1 = new SubTask("Купить семена", "Семена", Status.StatusType.NEW, firstEpic.getId());
        SubTask firstSubtask2 = new SubTask("Купить семена", "Семена", Status.StatusType.NEW, firstEpic.getId());
        SubTask firstSubtask3 = new SubTask("Купить семена", "Семена", Status.StatusType.NEW, firstEpic.getId());
        Task firstTask2 = new Task("Разработать лифт до луны", "Космолифт", Status.StatusType.NEW);
        Task firstTask3 = new Task("Разработать лифт до луны", "Космолифт", Status.StatusType.NEW);
        TaskManager manager = new TaskManager();
        manager.addTask(firstTask);
        manager.addEpic(firstEpic);
        manager.addSubTask(firstSubtask,firstEpic);
        manager.addSubTask(firstSubtask,firstEpic);
        manager.addSubTask(firstSubtask,firstEpic);
        manager.addSubTask(firstSubtask,firstEpic);
        manager.updateStatusEpic(Status.StatusType.NEW,firstEpic,firstSubtask);
        System.out.println(firstTask);


        System.out.println(firstEpic);






    }
}
