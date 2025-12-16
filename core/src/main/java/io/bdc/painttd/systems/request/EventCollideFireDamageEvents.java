package io.bdc.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.systems.*;
import io.bdc.painttd.utils.*;

import static io.bdc.painttd.game.Game.*;

@IsLogicProcess
public class EventCollideFireDamageEvents extends BaseSystem {
    public Object tokenCollideDamageEvent, tokenCollideSplashEvent, tokenCollideStainSplashEvent, tokenOnCollideDeadEvent;

    @All(value = {TeamComp.class, HealthComp.class})
    @Exclude(MarkerComp.Dead.class)
    Aspect collideAspect;

    @All(value = {MarkerComp.OnCollideDead.class})
    @Exclude(MarkerComp.Dead.class)
    Aspect onCollideDeadAspect;

    @All(value = {DamageSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect splashAspect;

    @All(value = {StainSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect stainAspect;

    ComponentMapper<PositionComp> positionMapper;
    ComponentMapper<DamageSplashComp> damageSplashMapper;
    ComponentMapper<StainSplashComp> stainSplashMapper;

    @Override
    protected void initialize() {
        super.initialize();

        tokenCollideDamageEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if (e.handled) return;
            if (!collideAspect.isInterested(world.getEntity(e.source)) || !collideAspect.isInterested(world.getEntity(e.target)))
                return;
            if (utils.isTeammateOrFriendly(e.source, e.target)) return;

            var event = EventTypes.collideDamageEvent;
            event.reset();
            event.source = e.source;
            event.target = e.target;
            Events.fire(event);
        });

        tokenOnCollideDeadEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if (!onCollideDeadAspect.isInterested(world.getEntity(e.source))) return;
            utils.markDead(e.source);
        });

        tokenCollideSplashEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if (e.handled) return;
            if (utils.isTeammateOrFriendly(e.source, e.target)) return;
            if (e.source == -1 || !splashAspect.isInterested(world.getEntity(e.source))) return;

            PositionComp pos = positionMapper.get(e.source);
            DamageSplashComp dmg = damageSplashMapper.get(e.source);
            if (dmg == null) return;

            var event = EventTypes.splashDamageEvent;
            event.reset();
            event.source = e.source;
            event.x = pos.x;
            event.y = pos.y;
            event.damage = dmg.damage;
            event.radius = dmg.radius;
            Events.fire(event);
        });

        tokenCollideStainSplashEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if (e.handled) return;
            if (e.source == -1 || !stainAspect.isInterested(world.getEntity(e.source))) return;

            PositionComp pos = positionMapper.get(e.source);
            StainSplashComp dmg = stainSplashMapper.get(e.source);

            var event = EventTypes.stainSplashDamageEvent;
            event.reset();
            event.source = e.source;
            event.x = pos.x;
            event.y = pos.y;
            event.damage = dmg.damage;
            event.radius = dmg.radius;
            Events.fire(event);
        });
    }

    @Override
    protected void dispose() {
        super.dispose();
        Events.off(tokenCollideDamageEvent);
        Events.off(tokenCollideSplashEvent);
        Events.off(tokenCollideStainSplashEvent);
    }

    @Override
    protected void processSystem() {
    }
}
