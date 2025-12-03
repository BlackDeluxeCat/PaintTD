package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.utils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.game.*;

import static io.bdc.painttd.content.components.marker.MarkerComp.*;

/**
 * 在每帧的开始重建四叉树.
 */
@IsLogicProcess
public class RebuildQuadTree extends BaseEntitySystem{
    public RebuildQuadTree(){
        super(Aspect.all(UseQuadTree.class, PositionComp.class, HitboxComp.class).exclude(Dead.class));
    }

    protected void process(int entityId){
        Game.entities.add(entityId);
    }

    @Override
    protected void processSystem(){
        Game.entities.create(world, -Game.map.outOfBoundSize, -Game.map.outOfBoundSize, Game.map.width + Game.map.outOfBoundSize, Game.map.height + Game.map.outOfBoundSize);
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for(int i = 0, s = actives.size(); s > i; i++){
            process(ids[i]);
        }
    }
}
