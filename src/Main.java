import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;


public class Main {
    private static TaskManager taskManager = Managers.getDefault();
    private static TaskManager back = Managers.getDefaultFileBackend();

    public static void main(String[] args) throws IOException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // месяцы начинаются с 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        File file = new File("task.txt");
        taskManager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", LocalDateTime.now(), Duration.ofHours(1)));
        taskManager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", LocalDateTime.now(), Duration.ofHours(1)));
        taskManager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", LocalDateTime.now(), Duration.ofHours(1)));
        taskManager.addTask(new Task("Выгулять собаку323", "Погулять с Джеком 20 минут", LocalDateTime.of(year, month, day, hour, minute), Duration.ofHours(1)));
        // taskManager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут",LocalDateTime.now(),Duration.ofMinutes(35)));
        back.addTask(new Task("Выгулять собак21у", "Погулять с Джеком 20 минут", LocalDateTime.now(), Duration.ofHours(1)));
        // FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
    }
}