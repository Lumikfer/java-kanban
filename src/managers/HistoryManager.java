package managers;

import task.Task;

import java.util.List;

public interface HistoryManager {

    boolean addTask(Task task);


    List<Task> getHistory();


    boolean remove(int id);

}
