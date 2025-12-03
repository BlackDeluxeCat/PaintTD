package io.bdc.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;

@IsLogicProcess
public class MovementVelGenFlowField extends IteratingSystem{
    private ComponentMapper<PositionComp> positionMapper;
    private ComponentMapper<VelocityComp> velocityMapper;
    private ComponentMapper<MoveSpeedComp> moveSpeedMapper;
    private ComponentMapper<MovementNextPathComp> nextPathMapper;

    public MovementVelGenFlowField(){
        super(Aspect.all(PositionComp.class, VelocityComp.class, MoveSpeedComp.class, MovementNextPathComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        PositionComp pos = positionMapper.get(entityId);
        VelocityComp vel = velocityMapper.get(entityId);
        MoveSpeedComp moveSpeed = moveSpeedMapper.get(entityId);
        MovementNextPathComp nextPath = nextPathMapper.get(entityId);

        //当前格无效或偏离当前格, 更新当前格
        if(nextPath.current.dst(pos.x, pos.y) > 0.71f){
            nextPath.current.set(Math.round(pos.x), Math.round(pos.y));
        }

        //当前格有效, 从流场读取下一格, 更新下一格, 单位向下一格移动
        if(nextPath.current.dst(pos.x, pos.y) <= 0.71f){
            nextPath.next.set(nextPath.current).add(Game.flowField.getDirection((int)nextPath.current.x, (int)nextPath.current.y, Vars.v1));

            vel.set(Vars.v1.set(nextPath.next.x - pos.x, nextPath.next.y - pos.y).nor().scl(moveSpeed.speed));
        }
    }
}
