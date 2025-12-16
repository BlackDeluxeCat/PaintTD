package io.bdc.painttd.systems.targeting;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.func.*;

/**
 * 一个基础索敌系统. 根据索敌标识使用过滤器筛选 **一个** 敌人.
 */
@IsLogicProcess
public class TargetSingleFind extends IteratingSystem {
    public ComponentMapper<TeamComp> tm;
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<TargetSingleComp> tcm;
    public ComponentMapper<TargetPriorityComp> tpm;
    public ComponentMapper<RangeComp> rm;

    protected final Vector2 entityPos = new Vector2();
    protected int currentProcessId;

    public TargetSingleFind() {
        super(Aspect.all(TeamComp.class, PositionComp.class, TargetSingleComp.class, TargetPriorityComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId) {
        currentProcessId = entityId;
        //TeamComp team = tm.get(entityId);
        TargetSingleComp current = tcm.get(entityId);
        TargetPriorityComp priority = tpm.get(entityId);
        PositionComp pos = pm.get(entityId);
        RangeComp range = rm.get(entityId);

        if (current.targetId != -1) {
            PositionComp epos = pm.get(current.targetId);
            if (epos != null) {
                if (Vars.v1.set(epos.x, epos.y).dst(pos.x, pos.y) > range.range) current.targetId = -1;
            }
        }

        if (current.targetId == -1 || Game.utils.isDead(current.targetId)) {
            entityPos.set(pos.x, pos.y);
            current.targetId = Game.entities.closestCircle(pos.x, pos.y, range.range, other -> {
                //排除友军
                if (!tm.has(other) || Game.utils.isTeammateOrFriendly(entityId, other)) return Float.MAX_VALUE;
                return getDistanceFilter(priority.priority).get(other);
            });
        }
    }

    public IntFloatf getDistanceFilter(int priority) {
        return switch (priority) {
            case TargetPriorityComp.CLOSEST -> (id) -> {
                if (currentProcessId == id) return Float.MAX_VALUE;  //排除自身
                PositionComp pos = pm.get(id);
                return Vars.v1.set(pos.x, pos.y).sub(entityPos).len2();
            };
            default -> (e) -> {
                return -1;
            };
        };
    }
}
