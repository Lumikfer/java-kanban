package task;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TaskParser {
    public static Task fromStringTask(String value) {
        String [] str = value.split(",");
        return new Task(
                str[2],
                str[4],
                Status.valueOf(str[3]),
                Integer.parseInt(str[0]),
                LocalDateTime.parse(str[6],
                        DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"))
                , Duration.parse(str[5])
        );
    }
    public static Epic fromStringEpic(String value) {
        String [] str = value.split(",");
        return new Epic(str[2],str[4],Status.valueOf(str[3]),Integer.parseInt(str[0]), LocalDateTime.parse(str[6]), Duration.parse(str[5]));
    }
    public static Subtask fromStringSub(String value) {
        String [] str = value.split(",");
        return new Subtask(str[2],str[4],Integer.parseInt(str[0]),Status.valueOf(str[3]),Integer.parseInt(str[5]), LocalDateTime.parse(str[7]), Duration.parse(str[6]));
    }
}
