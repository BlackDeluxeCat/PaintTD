package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

import static io.blackdeluxecat.painttd.game.Game.flowField;

public class FlowFieldUpdate extends BaseSystem{
    @Override
    protected void processSystem(){
        flowField.rebuild();
    }
}
