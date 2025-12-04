package io.bdc.painttd.content.trajector.context;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.game.*;

public class TargetSingleX extends VarInjector<Entity>{
    @Override
    public float get(@Null Entity object){
        TargetSingleComp target = Game.utils.targetSingleMapper.get(object);
        if(target == null || target.targetId == -1) return 0f;  //确实有可能连接到无法瞄准的单位, 防御性代码
        PositionComp pos = Game.utils.positionMapper.get(target.targetId);
        return pos.x;    //通常使用轨迹树的都有position组件
    }
}
