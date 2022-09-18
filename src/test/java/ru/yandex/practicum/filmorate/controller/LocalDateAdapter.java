package ru.yandex.practicum.filmorate.controller;

import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.time.LocalDate;

import static com.google.gson.stream.JsonToken.NULL;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public void write(com.google.gson.stream.JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if (localDate == null)
            jsonWriter.nullValue();
        else
            jsonWriter.value(localDate.toString());
    }

    @Override
    public LocalDate read(com.google.gson.stream.JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            String localeDateString = jsonReader.nextString();
            return LocalDate.parse(localeDateString);
        }
    }
}
