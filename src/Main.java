import Managers.Managers;
import Status.Status;
import Task.Epic;
import Task.Subtask;
import Task.Task;
import Managers.TaskManager;

public class Main {
    private static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        addTasks();

    }

    private static void addTasks() {
        Task task1 = new Task("asdasd", "door " +
                "muiy", Status.NEW);
        taskManager.addTask(task1);

        Task updateTask1 = new Task("kernak", "sektar",
                task1.getId(), Status.IN_PROGRESS);
        taskManager.updateTask(updateTask1);
        taskManager.addTask(new Task("sadasd", "123", Status.NEW));


        Epic doHomeWork = new Epic("321", "012");
        taskManager.addEpic(doHomeWork);
        Subtask doHomeWork1 = new Subtask("12", "3333", doHomeWork.getId(),
                Status.DONE, doHomeWork.getId());
        Subtask doHomeWork2 = new Subtask("2", "1111",
                doHomeWork.getId(), Status.DONE, doHomeWork.getId());
        Subtask doHomeWork3 = new Subtask("bueno", "123", doHomeWork.getId(),
                Status.NEW, doHomeWork.getId());
        taskManager.addSubtask(doHomeWork1);
        taskManager.addSubtask(doHomeWork2);
        taskManager.addSubtask(doHomeWork3);
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        Subtask doHomeWork4 = new Subtask("11111", "921",
                doHomeWork3.getId(), Status.DONE, doHomeWork.getId());
        taskManager.updateSubtask(doHomeWork4);
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(50));
        taskManager.removeSubtaskById(doHomeWork3.getId());
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        taskManager.removeEpicById(doHomeWork1.getId());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpics());
        Epic epic2 = new Epic("2222222", "333333", doHomeWork.getId());
        taskManager.updateEpic(epic2);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasks());
        Subtask subtask1 = new Subtask("012", "021", doHomeWork2.getId(), Status.NEW, doHomeWork.getId());
        taskManager.updateSubtask(subtask1);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasks());
        taskManager.addEpic(epic2);
        System.out.println(taskManager.getEpics());
        taskManager.removeEpicById(15);
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.removeSubtaskById(doHomeWork1.getId());
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasksForEpic(doHomeWork.getId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getTasksById(task1.getId()));
        System.out.println(taskManager.getSubtasksById(doHomeWork2.getId()));
        System.out.println(taskManager.getSubtasksById(doHomeWork2.getId()));
        System.out.println(taskManager.getEpicsById(doHomeWork1.getEpicId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());

    }
}