package io.bdc.painttd.content.trajector.context;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.game.*;

public class TargetPosY extends VarInjector<Entity>{
    @Override
    public float get(@Null Entity object){
        TargetPosComp target = Game.utils.targetPosMapper.get(object);
        if(target == null) return 0f;  //确实有可能连接到无法瞄准的单位, 防御性代码
        return target.y;    //通常使用轨迹树的都有position组件
    }
}
