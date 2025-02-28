package http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.TaskType;

import java.io.IOException;

public class AdapterForTaskType extends TypeAdapter<TaskType> {

    @Override
    public void write(JsonWriter writer, TaskType status) throws IOException {
        writer.value(status.name());
    }

    @Override
    public TaskType read(JsonReader reader) throws IOException {
        return TaskType.valueOf(reader.nextString().toUpperCase());
    }
}