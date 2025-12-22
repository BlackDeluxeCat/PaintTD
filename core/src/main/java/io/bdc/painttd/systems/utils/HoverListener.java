package io.bdc.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.render.*;

@Wire
public class HoverListener extends BaseSystem {
    @All(value = {MarkerComp.Hoverable.class, PositionComp.class, HitboxComp.class})
    EntitySubscription sub;

    ComponentMapper<PositionComp> posMapper;
    ComponentMapper<HitboxComp> hitboxMapper;

    public int hovered = -1, lastHovered;

    static Rectangle rect = new Rectangle();

    @Override
    protected void processSystem() {
        hovered = -1;
        IntBag ids = sub.getEntities();
        for (int i = 0; i < ids.size(); i++) {
            int e = ids.get(i);
            PositionComp pos = posMapper.get(e);
            HitboxComp hitbox = hitboxMapper.get(e);
            if (rect.setSize(hitbox.x(), hitbox.y()).setCenter(pos.x, pos.y)
                    .contains(Renderer.viewport.unproject(Vars.v1.set(Gdx.input.getX(), Gdx.input.getY()))
                    )) {
                hovered = e;
            }
        }
        if (lastHovered != hovered) {
            lastHovered = hovered;
            //Vars.hud.hoveredTable.build(hovered);
        }

    }
}
