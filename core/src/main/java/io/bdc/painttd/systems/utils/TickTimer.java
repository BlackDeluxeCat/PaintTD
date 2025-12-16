package io.bdc.painttd.systems.utils;

import com.artemis.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.systems.*;

@IsLogicProcess
public class TickTimer extends BaseSystem {
    @Override
    protected void processSystem() {
        Game.rules.ticks++;
    }
}
