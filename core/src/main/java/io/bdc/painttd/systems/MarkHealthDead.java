package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;

@IsLogicProcess
public class MarkHealthDead extends IteratingSystem {
    public ComponentMapper<HealthComp> hm;

    public MarkHealthDead() {
        super(Aspect.all(HealthComp.class).exclude(MarkerComp.Dead.class, TileStainComp.class));
    }

    @Override
    protected void process(int entityId) {
        if (hm.get(entityId).health <= 0) {
            Game.utils.markDead(entityId);
        }
    }
}
