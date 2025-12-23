package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.render.*;

public class DrawUnitHitbox extends IteratingSystem {
    public static boolean pref;

    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<HitboxComp> hbm;
    public ComponentMapper<HealthComp> hm;

    public DrawUnitHitbox() {
        super(Aspect.all(PositionComp.class).exclude(TileComp.class, TileStainComp.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        pref = Core.prefs.getBoolean("drawUnitHitbox");
    }

    @Override
    protected void process(int entityId) {
        PositionComp pos = pm.get(entityId);
        HealthComp hpc = hm.get(entityId);
        Vars.c1.set(Color.WHITE);
        if (hpc != null && hpc.maxHealth != -1) {
            Vars.c1.lerp(Color.RED, 1 - hpc.health / hpc.maxHealth).a = 1f;
        }
        HitboxComp hb = hbm.get(entityId);
        boolean hasSize = hb != null;
        float w = hasSize ? hb.x() : 1;
        float h = hasSize ? hb.y() : 1;
        Renderer.setColor(Vars.c1);
        Renderer.line.rect(pos.x - w / 2f, pos.y - h / 2f, w, h);
    }
}
