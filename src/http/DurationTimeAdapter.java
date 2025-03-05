package http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DurationTimeAdapter extends TypeAdapter<Duration> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        jsonWriter.value(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String[] parts = jsonReader.nextString().split(":");
        return Duration.ofHours(Long.parseLong(parts[0]))
                .plusMinutes(Long.parseLong(parts[1]))
                .plusSeconds(Long.parseLong(parts[2]));
    }
}