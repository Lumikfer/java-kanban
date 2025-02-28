package manager;

import tasks.*;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Collections;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {

    private int nextId;

    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        nextId = 1;
        historyManager = Managers.getDefaultHistory();
        Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()));
        prioritizedTasks = new TreeSet<>(taskComparator);
    }

    private int getNextId() {
        return nextId++;
    }

    // create
    @Override
    public Task addTask(Task task) {
        if (task.getStartTime() != null && isTaskOverlapping(task, task.getId())) {
            throw new IllegalStateException("Задача пересекается по времени с другой задачей.");
        }
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        if (subtask.getStartTime() != null && isTaskOverlapping(subtask, subtask.getId())) {
            throw new IllegalStateException("Подзадача пересекается по времени с другой задачей.");
        }
        subtask.setId(getNextId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateEpicsStatus(epic);
        return subtask;
    }

    // update
    @Override
    public Task updateTask(Task task) {
        int taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        Task oldTask = tasks.get(taskId);
        if (oldTask.getStartTime() != null) {
            prioritizedTasks.remove(oldTask);
        }
        if (task.getStartTime() != null && isTaskOverlapping(task, taskId)) {
            prioritizedTasks.add(oldTask);
            throw new IllegalStateException("Задача пересекается по времени с другой задачей.");
        }
        tasks.replace(taskId, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        int epicId = epic.getId();
        if (!epics.containsKey(epicId)) {
            return null;
        }
        Epic oldEpic = epics.get(epicId);
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = subtasks.get(subtaskId);
        if (!subtasks.containsKey(subtaskId) || oldSubtask.getEpicId() != epicId) {
            return null;
        }
        if (oldSubtask.getStartTime() != null) {
            prioritizedTasks.remove(oldSubtask);
        }
        if (subtask.getStartTime() != null && isTaskOverlapping(subtask, subtaskId)) {
            prioritizedTasks.add(oldSubtask);
            throw new IllegalStateException("Задача пересекается по времени с другой задачей.");
        }
        Epic epic = epics.get(epicId);
        epic.removeSubtask(oldSubtask);
        subtasks.replace(subtaskId, subtask);
        epic.addSubtask(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateEpicsStatus(epic);
        return subtask;
    }

    // getList
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        return epic == null ? Collections.emptyList() : new ArrayList<>(epic.getSubtaskList());
    }

    // getById
    @Override
    public Task getTaskByID(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicByID(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    // removeById
    @Override
    public void removeTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return;
        }
        epic.getSubtaskList().forEach(subtask -> {
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return;
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
        }
        int epicId = subtask.getEpicId();
        subtasks.remove(id);
        historyManager.remove(id);
        Epic epic = epics.get(epicId);
        epic.removeSubtask(subtask);
        updateEpicsStatus(epic);
    }

    // deleteAll
    @Override
    public void deleteTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.values().forEach(epic -> {
            epic.getSubtaskList().forEach(subtask -> {
                historyManager.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
            });
            historyManager.remove(epic.getId());
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.clearSubtasks();
            updateEpicsStatus(epic);
        });
    }

    // updateStatus
    private void updateEpicsStatus(Epic epic) {
        List<Subtask> subtasksList = epic.getSubtaskList();
        if (subtasksList.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        long countDoneTask = subtasksList.stream().filter(subtask -> subtask.getStatus() == TaskStatus.DONE).count();
        long countNewTask = subtasksList.stream().filter(subtask -> subtask.getStatus() == TaskStatus.NEW).count();

        if (countNewTask == subtasksList.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDoneTask == subtasksList.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private boolean isTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2)) && !(end2.isBefore(start1));
    }

    @Override
    public boolean isTaskOverlapping(Task newTask, int ignoreTaskId) {
        if (newTask.getStartTime() == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(task -> task.getId() != ignoreTaskId)
                .anyMatch(existingTask -> isTasksOverlap(newTask, existingTask));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void addTaskFromFile(Task task) {
        tasks.put(task.getId(), task);
    }

    protected void addEpicFromFile(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void addSubtaskFromFile(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
    }

    protected void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
}

