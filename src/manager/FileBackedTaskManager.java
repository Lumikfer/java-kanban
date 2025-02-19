package manager;

import task.*;
import util.FormatterUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager  {
    private final File fileName = new File("task.txt");
    private static final String HEAD_CSV = "id,type,name,status,description,epic,duration (minutes),startTime, endTime";

    public FileBackedTaskManager() {

    }

    private void save() {
        try (Writer fileWriter = new FileWriter(this.fileName)) {
            fileWriter.write(HEAD_CSV);
            fileWriter.write("\n");
            for (Long id : tasks.keySet()) {
                fileWriter.write(FormatterUtil.toString(tasks.get(id)));
                fileWriter.write("\n");
            }
            for (Long id : epicTasks.keySet()) {
                fileWriter.write(FormatterUtil.toString(epicTasks.get(id)));
                fileWriter.write("\n");
            }
            for (Task subtask : getListSubTasks()) {
                fileWriter.write(FormatterUtil.toString(subtask));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager manager = new FileBackedTaskManager();
            List<String> list = new ArrayList<>();
            String head = "id,type,name,status,description,epic,duration (minutes),startTime, endTime";
            while (br.ready()) {
                list.add(br.readLine());
            }

            list.remove(head);
            for (String str : list) {
                String[] s = str.split(",");
                String task = TaskType.TASK.toString();
                String subtask = TaskType.SUBTASK.toString();
                String epic = TaskType.EPIC.toString();
                if (s[1].equals(task)) {
                    manager.createTask(FormatterUtil.fromStringTask(str));
                } else if (s[1].equals(epic)) {
                    manager.createEpicTask(FormatterUtil.fromStringEpic(str));
                } else if (s[1].equals(subtask)) {
                    manager.createSubTask(FormatterUtil.fromStringSub(str));
                } else {
                    System.out.println("Eror");
                }
            }
            return manager;
        } catch (IOException e) {
           System.out.println("Возникла ошибка  загрузки данных из файла");
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        save();
    }

    @Override
    public void createSubTask(Subtask subtask) {
        save();
    }

    @Override
    public void createEpicTask(Epic epic) {
        save();
    }





    @Override
    public Task deleteTaskById(Long id) {
        save();
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpicTaskById(Long id) {
        Epic epic = super.deleteEpicTaskById(id);
        save();
        return epic;
    }

    @Override
    public void deleteSubTaskById(Long id) {
         super.deleteSubTaskById(id);
        save();

    }

    @Override
    public Task deleteTask(Task task) {
        Task deletedTask = super.deleteTask(task);
        save();
        return deletedTask;
    }

    @Override
    public Epic deleteEpicTask(Epic task) {
        Epic epic = super.deleteEpicTask(task);
        save();
        return epic;
    }

    @Override
    public void deleteSubTask(Subtask task) {
       super.deleteSubTask(task);
        save();

    }

    @Override
    public void clearListTasks() {
        super.clearListTasks();
        save();
    }

    @Override
    public void clearListEpicTasks() {
        super.clearListEpicTasks();
        save();
    }

    @Override
    public void clearListSubTasks(Epic epic) {
        super.clearListSubTasks(epic);
        save();
    }

}
