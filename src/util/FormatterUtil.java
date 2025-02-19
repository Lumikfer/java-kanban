package util;

import task.*;

import java.time.LocalDateTime;


import static task.Task.DATE_TIME_FORMATTER;

public class FormatterUtil {

    public static String toString(Task task) {
        String epicId = "";
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            Subtask sbTask = (Subtask) task;
        } else if (task.getTaskType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
        }
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + epicId + "," + task.getDuration().toMinutes() + "," + startTime.format(DATE_TIME_FORMATTER) + "," + endTime.format(DATE_TIME_FORMATTER);
    }

    public static Task fromStringTask(String value) {
        String[] str = value.split(",");
        return new Task(str[2], str[4], Status.valueOf(str[3]), Integer.parseInt(str[0]));
    }

    public static Epic fromStringEpic(String value) {
        String[] str = value.split(",");
        return new Epic(str[2], str[4], Status.valueOf(str[3]), Integer.parseInt(str[0]));
    }

    public static Subtask fromStringSub(String value) {
        String[] str = value.split(",");
        return new Subtask(str[2], str[4], Integer.parseInt(str[0]), Status.valueOf(str[3]), Integer.parseInt(str[5]));
    }
}
