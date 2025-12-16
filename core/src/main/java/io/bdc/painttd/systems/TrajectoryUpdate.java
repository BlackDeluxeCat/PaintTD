package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;

public class TrajectoryUpdate extends IteratingSystem {
    public ComponentMapper<TrajectoryComp> tm;

    public TrajectoryUpdate() {
        super(Aspect.all(TrajectoryComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId) {
        var net = tm.get(entityId).nodeGraph;
        net.update(1f);
    }
}
