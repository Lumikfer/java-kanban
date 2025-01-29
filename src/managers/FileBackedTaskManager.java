package managers;
import exception.ManagerLoadFromFileException;
import exception.ManagerSaveException;
import task.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileTask;
    public  FileBackedTaskManager() {
        this.fileTask = new File("task.txt");
    }
    public FileBackedTaskManager(File file) throws IOException {
        this.fileTask = file;
        try (FileWriter fr = new FileWriter(fileTask)) {
            fr.write("id, type, name, status, description, epic");
        }
    }
    public  static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadFromFileException {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager manager = new FileBackedTaskManager();
            List<String> list = new ArrayList<>();
            String head ="id, type, name, status, description, epic";
            while(br.ready()){
                list.add(br.readLine());
            }
            list.remove(head);
            for(String str:list) {
                String[] s = str.split(",");
                String task = TaskEnum.TASK.toString();
                String subtask = TaskEnum.SUBTASK.toString();
                String epic = TaskEnum.EPIC.toString();
                if(s[1].equals(task)){manager.addTask(Task.fromString(str));}
                else if(s[1].equals(epic)){manager.addEpic(Epic.fromString(str));}
                else if(s[1].equals(subtask)){manager.addSubtask(Subtask.fromString(str));}
                else {
                    System.out.println("Eror");
                }
            }
            return manager;
        }catch(IOException e) {
            throw new ManagerLoadFromFileException("Возникла ошибка  загрузки данных из файла", file);
        }
    }

    private void save() throws ManagerSaveException {
        try(FileWriter fr = new FileWriter(fileTask)) {
            String head = "id, type, name, status, description, epic\n";
            fr.write(head);
            for (Task task : getTasks()) {
                fr.write(String.format("%s\n", task.toString()));
            }
            for (Epic epic : getEpics()) {
                fr.write(String.format("%s\n", epic.toString()));
            }
            for (Subtask subtask : getSubtasks()) {
                fr.write(String.format("%s\n", subtask.toString()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Возникла ошибка при автосохранении менеджера", fileTask);
        }
    }
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }
    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }
    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }
    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }
    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }
    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }
}
