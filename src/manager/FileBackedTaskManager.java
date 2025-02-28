package manager;

import exceptions.*;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
    }

    @Override
    public void removeEpicByID(int id) {
        super.removeEpicByID(id);
        save();
    }

    @Override
    public void removeSubtaskByID(int id) {
        super.removeSubtaskByID(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    private void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic,duration, startTime\n");

            for (Task task : getTasks()) {
                sb.append(taskToString(task)).append("\n");
            }

            for (Epic epic : getEpics()) {
                sb.append(taskToString(epic)).append("\n");
            }

            for (Subtask subtask : getSubtasks()) {
                sb.append(taskToString(subtask)).append("\n");
            }

            if (sb.isEmpty()) {
                throw new ManagerSaveException("Файл пуст");
            }

            Files.writeString(file.toPath(), sb.toString());

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла", e);
        }
    }

    private String taskToString(Task task) {
        String type;
        String epicId = "";
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof Subtask) {
            type = "SUBTASK";
            epicId = String.valueOf(((Subtask) task).getEpicId());
        } else {
            type = "TASK";
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", task.getId(), type, task.getName(), task.getStatus(), task.getDescription(), epicId, task.getDuration(), task.getStartTime());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            if (content.isEmpty()) {
                throw new ManagerSaveException("Файл пуст");
            }
            String[] lines = content.split("\n");
            int maxId = 0;

            for (int i = 1; i < lines.length; i++) {
                Task task = manager.fromString(lines[i]);

                if (task.getId() > maxId) {
                    maxId = task.getId();
                }

                if (task instanceof Epic) {
                    manager.addEpicFromFile((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtaskFromFile((Subtask) task);
                } else {
                    manager.addTaskFromFile(task);
                }
            }

            manager.updateNextId(maxId);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла", e);
        }
        return manager;
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        String epicId = "";
        Duration duration;
        LocalDateTime startTime;
        if (parts.length > 7) {
            epicId = parts[5];
            duration = Duration.parse(parts[6]);
            startTime = LocalDateTime.parse(parts[7]);
        } else {
            duration = Duration.parse(parts[5]);
            startTime = LocalDateTime.parse(parts[6]);
        }

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, status, duration, startTime);
                task.setId(id);
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(name, description, status, Integer.parseInt(epicId), duration, startTime);
                subtask.setId(id);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}