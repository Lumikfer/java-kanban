package task;

import status.Status;

public class TaskParser {
    public static Task fromStringTask(String value){
        String [] str = value.split(",");
        return new Task(str[2],str[4], Status.valueOf(str[3]),Integer.parseInt(str[0]));
    }
    
    public static Epic fromStringEpic(String value){
        String [] str = value.split(",");
        return new Epic(str[2],str[4],Status.valueOf(str[3]),Integer.parseInt(str[0]));
    }
    
    public static Subtask fromStringSub(String value){
        String [] str = value.split(",");
        return new Subtask(str[2],str[4],Integer.parseInt(str[0]),Status.valueOf(str[3]),Integer.parseInt(str[5]));
    }
}
