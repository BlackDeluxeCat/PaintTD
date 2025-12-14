package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;

public class MovementVelGenTrajectory extends IteratingSystem{
    ComponentMapper<TrajectoryComp> trajectoryMapper;
    ComponentMapper<VelocityComp> velocityMapper;

    public MovementVelGenTrajectory(){
        super(Aspect.all(TrajectoryComp.class, VelocityComp.class, PositionComp.class));
    }

    @Override
    protected void process(int entityId){
        TrajectoryComp trajectory = trajectoryMapper.get(entityId);
        VelocityComp velocity = velocityMapper.get(entityId);
        velocity.set(trajectory.nodeGraph.output);
    }
}
