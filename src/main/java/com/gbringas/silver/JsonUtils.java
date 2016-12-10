package com.gbringas.silver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

public class JsonUtils {

    public static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(File.class, new TypeAdapter<File>() {
        public void write(JsonWriter jsonWriter, File file) throws IOException {
            if (file == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(file.getAbsolutePath());
            }

        }

        public File read(JsonReader jsonReader) throws IOException {
            return new File(jsonReader.nextString());
        }
    }).enableComplexMapKeySerialization().setPrettyPrinting().create();

    private JsonUtils() {
    }
}
