package util;

import statuses.StatusTask;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class StringFormatter {

    public static Task taskFromString(String value) {
        String[] taskInfo = value.split(",");
        if (taskInfo[6].equals("null")) {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    LocalDateTime.parse(taskInfo[7]));
        }  else {
            return new Task(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    Duration.parse(taskInfo[6]), LocalDateTime.parse(taskInfo[7]));
        }
    }

    public static Epic epicFromString(String value) {
        String[] taskInfo = value.split(",");
        if (taskInfo[6].equals("null")) {
            return new Epic(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    LocalDateTime.parse(taskInfo[7]));
        }  else {
            return new Epic(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[0]),
                    Duration.parse(taskInfo[6]), LocalDateTime.parse(taskInfo[7]));
        }
    }

    public static Subtask subFromString(String value) {
        String[] taskInfo = value.split(",");
        if (taskInfo[6].equals("null")) {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]), LocalDateTime.parse(taskInfo[7]));
        }  else {
            return new Subtask(taskInfo[2], taskInfo[4], StatusTask.valueOf(taskInfo[3]), Integer.parseInt(taskInfo[5]),
                    Integer.parseInt(taskInfo[0]), Duration.parse(taskInfo[6]), LocalDateTime.parse(taskInfo[7]));
        }
    }

}
