package io.blackdeluxecat.painttd.content;

import com.badlogic.gdx.utils.*;

public class Trajectory{
    public static ObjectMap<String, TrajectoryProcessor> processors = new ObjectMap<>();
    public static ObjectMap<TrajectoryProcessor, String> processorNames = new ObjectMap<>();

    public static void register(String name, TrajectoryProcessor processor){
        processors.put(name, processor);
        processorNames.put(processor, name);
    }

    public abstract static class TrajectoryProcessor{
        public boolean hasChildren = false;

        public abstract void update(float deltaTicks, TrajectoryTree.TrajectoryNode node);

        public void copyTo(TrajectoryTree.TrajectoryNode node, TrajectoryTree.TrajectoryNode copy){
        }
    }
}

