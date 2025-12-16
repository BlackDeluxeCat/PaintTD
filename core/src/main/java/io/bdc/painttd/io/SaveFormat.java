package io.bdc.painttd.io;

import com.artemis.io.*;
import io.bdc.painttd.map.*;

public class SaveFormat extends SaveFileFormat {
    public Rule rules;

    public SaveFormat() {
    }

    public SaveFormat(int build, int saveVersion) {
        var meta = new Metadata();
        meta.build = build;
        meta.version = saveVersion;
    }

    public Metadata getMetadata() {
        return (Metadata)metadata;
    }

    public static class Metadata extends SaveFileFormat.Metadata {
        public int build;

        public String name;
        public String description;
        public String author;
    }
}
