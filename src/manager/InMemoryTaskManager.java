package manager;

import exception.IntersectionException;
import task.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private long id = 0;
    protected static  HashMap<Long, Task> tasks = new HashMap<>();
    protected  static  Map<Long,Subtask> subTasks = new HashMap<>();
    protected static  HashMap<Long, Epic> epicTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(Task task) {
        id += 1;
        tasks.put(id, task);
        addPriorityTask(task);
    }

    @Override
    public void createEpicTask(Epic epic) {
        id += 1;
        epicTasks.put(id, epic);

    }

    @Override
    public void createSubTask(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            id += 1;
           subTasks.put(id,subtask);
            addPriorityTask(subtask);
        }
    }

    @Override
    public List<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getListEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<Task> getListSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasksId();
        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int allDoneCount = 0;
        int allNewCount = 0;
        for (int id : subtasksId) {
            Subtask subtask = subTasks.get(id);
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
    public Task getTaskById(Long id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicTaskById(Long id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(Long id) {
        Subtask subtask = subTasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subTasks.get(id);
    }

    @Override
    public Task deleteTaskById(Long id) {
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpicTaskById(Long id) {
        return epicTasks.remove(id);
    }

    @Override
    public void deleteSubTaskById(Long id) {
        Subtask subtask = subTasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            if (epicId != -1) {
                Epic epic = epicTasks.get(epicId);
                if (epic != null) {
                    ArrayList<Integer> id = epic.getSubtasksId();
                    if (id != null) {
                        epic.removeSubtaskById(id);
                        updateEpicStatus(epic);
                    }
                }
            }
            subTasks.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public Task deleteTask(Task task) {
        return deleteTaskById(task.getId());
    }

    @Override
    public Epic deleteEpicTask(Epic task) {
        return deleteEpicTaskById(task.getId());
    }

    @Override
    public void deleteSubTask(Subtask task) {
         deleteSubTaskById(task.getId());
    }

    public void moveTaskToProgress(Long id) {
        getTaskById(id).setStatus(Status.IN_PROGRESS);
    }

    public void moveSubTaskToProgress(Long id) {
        getSubTaskById(id).setStatus(Status.IN_PROGRESS);
    }

    public void moveTaskToDone(Long id) {
        getTaskById(id).setStatus(Status.DONE);
    }

    public void moveSubTaskToDone(Long id) {
        getSubTaskById(id).setStatus(Status.DONE);
    }

    @Override
    public void clearListTasks() {
        tasks.clear();
    }

    @Override
    public void clearListEpicTasks() {
        epicTasks.clear();
    }

    @Override
    public void clearListSubTasks(Epic epic) {
        epic.clearSubtasksId();
    }

    private void addPriorityTask(Task task) {
        priorityTasks.add(task);
        checkIntersectionTask(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return priorityTasks.stream().toList();
    }

    private void checkIntersectionTask(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        tasks.forEach(taskStream -> {
            if (task.getStartTime() != null && task.getEndTime() != null && taskStream.getStartTime() != null && taskStream.getEndTime() != null && !task.equals(taskStream)) {
                if (task.getStartTime().equals(taskStream.getStartTime())
                        || task.getEndTime().equals(taskStream.getEndTime())
                        || (task.getStartTime().isAfter(taskStream.getStartTime()) && task.getStartTime().isBefore(taskStream.getEndTime()))
                        || (task.getEndTime().isAfter(taskStream.getStartTime()) && task.getEndTime().isBefore(taskStream.getEndTime()))
                        || (taskStream.getStartTime().isAfter(task.getStartTime()) && taskStream.getEndTime().isBefore(task.getEndTime()))
                        || (task.getStartTime().isAfter(taskStream.getStartTime()) && task.getEndTime().isBefore(taskStream.getEndTime()))
                ) {
                    throw new IntersectionException("Пересечение задач");
                }
            }
        });
    }
}


