package io.bdc.painttd.io;

import com.badlogic.gdx.utils.*;

public class JsonIO {
    public static Json json = new Json();
    public static JsonReader reader = new JsonReader();

    public static void load() {
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("str", String.class);
        json.addClassTag("i", Integer.class);
        json.addClassTag("f", Float.class);
        json.addClassTag("b", Boolean.class);
        json.addClassTag("l", Long.class);
        json.addClassTag("d", Double.class);
    }
}
