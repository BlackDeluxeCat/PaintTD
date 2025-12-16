package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.content.components.logic.*;

import static io.bdc.painttd.game.Game.*;

@IsLogicProcess
public class FlowFieldCoreChangeDetect extends IteratingSystem {
    public ComponentMapper<TileStainComp> tsc;
    public ComponentMapper<PositionComp> pc;

    public FlowFieldCoreChangeDetect() {
        super(Aspect.all(TileStainComp.class, PositionComp.class));
    }

    @Override
    protected void process(int entityId) {
        TileStainComp ts = this.tsc.get(entityId);
        if (ts.isCore != ts.lastIsCore) {
            ts.lastIsCore = ts.isCore;
            PositionComp p = pc.get(entityId);
            flowField.change(p.tileX(), p.tileY());
        }
    }
}
