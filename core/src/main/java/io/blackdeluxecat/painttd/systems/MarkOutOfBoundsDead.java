package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;

public class MarkOutOfBoundsDead extends IteratingSystem{
    ComponentMapper<PositionComp> positionMapper;

    public MarkOutOfBoundsDead(){
        super(Aspect.all(PositionComp.class));
    }

    @Override
    protected void process(int entityId){
        PositionComp position = positionMapper.get(entityId);
        if(position.x < -Game.map.outOfBoundSize || position.x >= Game.map.width + Game.map.outOfBoundSize || position.y < -Game.map.outOfBoundSize || position.y >= Game.map.width + Game.map.outOfBoundSize){
            Game.utils.markDead(entityId);
        }
    }
}
