package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

@IsLogicProcess
public class ShootAtkSingleFireSplashTileStain extends IteratingSystem {
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<StainSplashComp> stainSplashMapper;
    ComponentMapper<TeamComp> teamMapper;

    public ShootAtkSingleFireSplashTileStain() {
        super(Aspect.all(CooldownComp.class, TargetSingleComp.class, StainSplashComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId) {
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if (cooldown.shootCount > 0) {
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if (targetSingle.targetId != -1) {
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                StainSplashComp splash = stainSplashMapper.get(entityId);

                for (int i = 0; i < cooldown.shootCount; i++) {
                    var event = EventTypes.stainSplashDamageEvent;
                    event.reset();
                    event.source = entityId;
                    event.team = teamMapper.get(entityId).team;
                    event.x = tgtPos.tileX();
                    event.y = tgtPos.tileY();
                    event.damage = splash.damage;
                    event.radius = splash.radius;
                    Events.fire(event);
                }
            }
        }

    }
}
