package io.bdc.painttd.content.trajector.context;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.game.*;

public class EntityPositionY extends VarInjector<Entity>{
    @Override
    public float get(@Null Entity object){
        return Game.utils.positionMapper.get(object).y;
    }
}
