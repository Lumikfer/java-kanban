package managers;

import status.Status;
import task.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextID = 0;
    
    @Override
    public void addTask(Task task) {
        if (intersectionеTask(task) == false) {
            System.out.println("низя");
            return;
        }
        task.setId(nextID++);
        tasks.put(task.getId(), task);
    }

    public boolean intersectionеTask(Task task) {
        TreeSet<Task> tasksk = getPrioritizedTasks();
        tasksk.stream()
                .filter(n -> n.getStartTime().isBefore(task.getStartTime()))
                .filter(n -> n.getEndTime().isAfter(task.getEndTime()))
                .collect(Collectors.toSet());
        return tasksk.isEmpty();
    }

    public boolean intersectionеEpic(Epic epic) {
        TreeSet<Epic> epicsc = getPrioritizedEpic();
        epicsc.stream()
                .filter(n -> n.getStartTime().isBefore(epic.getStartTime()))
                .filter(n -> n.getEndTime().isAfter(epic.getEndTime()))
                .collect(Collectors.toSet());
        return epicsc.isEmpty();
    }

    @Override
    public void addEpic(Epic epic) {
        if (intersectionеEpic(epic) == false) {
            System.out.println("низя");
            return;
        }
        epic.setId(nextID++);
        epics.put(epic.getId(), epic);
        epic.setStartTime(subtasks.get(0).getStartTime());
        epic.setEndTime(subtasks.get(subtasks.size()).getEndTime());
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        }

        subtask.setId(nextID++);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);

    }

    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.put(task.getId(), task);


    }


    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        if (!epics.containsKey(epicId)) {
            return;
        }
        Epic updatedEpic = epics.get(epicId);
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (!subtasks.containsKey(subtaskId)) {
            return;
        }
        Subtask subtaskToRemove = subtasks.get(subtaskId);
        int epicId = subtaskToRemove.getEpicId();
        if (epicId == subtask.getEpicId()) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(epicId);
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task getTasksById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.addTask(task);
        }
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicsById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.addTask(epic);
        }
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.addTask(subtask);
        }
        return subtasks.get(subtaskId);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManager.remove(taskId);
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        historyManager.remove(epicId);
        if (!epics.containsKey(epicId)) {
            return;
        }
        ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasksId();
        for (Integer id : epicSubtasks) {
            subtasks.remove(id);
            historyManager.remove(id);
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            if (epicId != -1) {
                Epic epic = epics.get(epicId);
                if (epic != null) {
                    ArrayList<Integer> id = epic.getSubtasksId();
                    if (id != null) {
                        epic.removeSubtaskById(subtaskId);
                        updateEpicStatus(epic);
                    }
                }
            }
            subtasks.remove(subtaskId);
        }
        historyManager.remove(subtaskId);
    }

    @Override
    public void removeAllTasks() {
        for (int z : tasks.keySet()) {
            historyManager.remove(z);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (int z : epics.keySet()) {
            historyManager.remove(z);
        }
        for (int k : subtasks.keySet()) {
            historyManager.remove(k);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (int z : subtasks.keySet()) {
            historyManager.remove(z);
        }
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public List<Subtask> getSubtasksForEpic(int epicId) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtaskList.add(subtask);
                }
            }
        }
        return subtaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasksId();
        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int allDoneCount = 0;
        int allNewCount = 0;
        for (int id : subtasksId) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getStatus() == Status.DONE) {
                allDoneCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allNewCount++;
            }
        }
        if (allDoneCount == subtasksId.size()) {
            epic.setStatus(Status.DONE);
        } else if (allNewCount == subtasksId.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        List<Task> tas = tasks.values().stream()
                .filter(ta -> !ta.getEndTime().equals(null))
                .collect(Collectors.toList());
        Comparator<Task> comparator = new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        };

        TreeSet<Task> set = new TreeSet<>(comparator);
        set.addAll(tas);
        return set;
    }

    @Override
    public TreeSet<Epic> getPrioritizedEpic() {
        List<Epic> tas = epics.values().stream()
                .filter(ta -> !ta.getEndTime().equals(null))
                .collect(Collectors.toList());
        Comparator<Task> comparator = new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        };

        TreeSet<Epic> set = new TreeSet<>(comparator);
        set.addAll(tas);
        return set;
    }
