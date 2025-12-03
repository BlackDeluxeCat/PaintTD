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
public class ShootAtkSingleFireSplashDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageSplashComp> damageSplashMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public ShootAtkSingleFireSplashDamage(){
        super(Aspect.all(CooldownComp.class, DamageSplashComp.class, TargetSingleComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                DamageSplashComp splash = damageSplashMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    var event = EventTypes.splashDamageEvent;
                    event.reset();
                    event.source = entityId;
                    event.x = tgtPos.x;
                    event.y = tgtPos.y;
                    event.damage = splash.damage;
                    event.radius = splash.radius;
                    Events.fire(event);
                }
            }
        }
    }
}
