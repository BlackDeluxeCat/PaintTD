package io.blackdeluxecat.painttd.content;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.trajector.*;

public class Trajectories{
    public static ObjectMap<String, Processor> processors = new ObjectMap<>();
    public static ObjectMap<Processor, String> processorNames = new ObjectMap<>();

    public static void register(String name, Processor processor){
        processors.put(name, processor);
        processorNames.put(processor, name);
    }
}

