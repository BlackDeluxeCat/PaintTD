package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.render.*;

public class DrawTarget extends IteratingSystem {
    public static boolean drawRange, drawTargetLine;

    public ComponentMapper<TargetSingleComp> tm;
    public ComponentMapper<CooldownComp> cm;
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<RangeComp> rm;

    public DrawTarget() {
        super(Aspect.all(TargetSingleComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        drawRange = Core.prefs.getBoolean("drawRange", true);
        drawTargetLine = Core.prefs.getBoolean("drawTargetLine", true);
    }

    @Override
    protected void process(int entityId) {
        TargetSingleComp target = tm.get(entityId);
        PositionComp pos = pm.get(entityId), targetPos;

        if (drawTargetLine && target.targetId != -1) {
            targetPos = pm.get(target.targetId);

            CooldownComp cd = cm.get(entityId);
            float a = cd == null ? 1f : (1 - cd.currentCooldown / cd.cooldown);
            if (a > 0.5f) {
                Renderer.setColor(Color.RED, a);
                Renderer.line.line(pos.x, pos.y, targetPos.x, targetPos.y);
            }
        }

        RangeComp range = rm.get(entityId);
        if (drawRange && range != null) {
            Renderer.setColor(Color.DARK_GRAY, 0.5f);
            Renderer.line.circle(pos.x, pos.y, range.range, 32);
        }
    }
}
