package io.bdc.painttd.systems;

import com.artemis.*;

import static io.bdc.painttd.game.Game.*;

@IsLogicProcess
public class FlowFieldUpdate extends BaseSystem{
    @Override
    protected void processSystem(){
        flowField.rebuild();
    }
}
