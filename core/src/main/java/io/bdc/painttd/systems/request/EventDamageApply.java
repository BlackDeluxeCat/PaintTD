package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.game.Game.*;

@IsLogicProcess
public class EventDamageApply extends BaseSystem{
    public Object tokenCollideDamage, tokenSplashDamage, tokenDirectDamage, tokenSplashTileStainDamage, tokenTileStainDamage;

    @All(value = {HealthComp.class})
    Aspect damagableAspect;

    ComponentMapper<HealthComp> healthMapper;

    IntArray tmp = new IntArray();

    @Override
    protected void initialize(){
        super.initialize();

        tokenCollideDamage = Events.on(EventTypes.CollideDamageEvent.class, e -> {
            if(e.handled) return;

            if(world.getMapper(MarkerComp.BulletProjected.class).has(e.source) && world.getMapper(TileStainComp.class).has(e.target)){
                Gdx.app.debug("", e.source + " " + e.target);
            }
            if(healthMapper.has(e.source) && healthMapper.has(e.target)){
                var sourceHealth = healthMapper.get(e.source);
                var targetHealth = healthMapper.get(e.target);
                if(sourceHealth.health <= 0 || targetHealth.health <= 0){
                    return;
                }
                float damage = Math.min(sourceHealth.health, targetHealth.health);
                targetHealth.health -= damage;
                sourceHealth.health -= damage;
                e.handle();
            }
        });

        tokenSplashDamage = Events.on(EventTypes.SplashDamageEvent.class, e -> {
            if(e.handled) return;
            entities.eachCircle(e.x, e.y, e.radius,
                other -> !utils.isTeammateOrFriendly(e.source, other) && damagableAspect.isInterested(world.getEntity(other)),
                other -> {
                    var event = EventTypes.damageEvent;
                    event.reset();
                    event.source = e.source;
                    event.target = other;
                    event.damage = e.damage;
                    Events.fire(event);
                });
            e.handle();
        });

        tokenDirectDamage = Events.on(EventTypes.DamageEvent.class, e -> {
            if(e.handled) return;
            if(healthMapper.has(e.target)){
                var targetHealth = healthMapper.get(e.target);
                if(targetHealth.health <= 0)
                    return;

                targetHealth.health -= e.damage;
                e.handle();
            }
        });

        tokenSplashTileStainDamage = Events.on(EventTypes.StainSplashDamageEvent.class, e -> {
            tmp.clear();
            map.queryCircle(map.stains, Math.round(e.x), Math.round(e.y), (int)e.radius, tmp);
            for(int j = 0; j < tmp.size; j++){
                int tileStain = tmp.get(j);
                var event = EventTypes.stainDamageEvent;
                event.reset();
                event.source = e.source;
                event.target = tileStain;
                event.damage = e.damage;
                event.team = e.team;
                Events.fire(event);
            }
        });

        tokenTileStainDamage = Events.on(EventTypes.StainDamageEvent.class, e -> {
            if(e.handled) return;
            if(e.target != -1){
                utils.putTileStain(e.target, e.team, e.damage);
            }
        });
    }

    @Override
    protected void dispose(){
        super.dispose();
        Events.off(tokenCollideDamage);
        Events.off(tokenSplashDamage);
        Events.off(tokenDirectDamage);
        Events.off(tokenSplashTileStainDamage);
        Events.off(tokenTileStainDamage);
    }

    @Override
    protected void processSystem(){
    }
}
