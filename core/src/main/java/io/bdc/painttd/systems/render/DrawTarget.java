package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;

import static io.bdc.painttd.Core.*;

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

        if (target.targetId != -1) {
            targetPos = pm.get(target.targetId);

            CooldownComp cd = cm.get(entityId);
            float a = cd == null ? 1f : (1 - cd.currentCooldown / cd.cooldown);
            if (a > 0.5f) {
                shaper.begin(ShapeRenderer.ShapeType.Line);
                shaper.getColor().set(Color.RED, a);

                shaper.setColor(Color.RED);
                shaper.line(Vars.v1.set(pos.x, pos.y), Vars.v2.set(targetPos.x, targetPos.y));
                shaper.end();
            }
        }

        RangeComp range = rm.get(entityId);
        if (range != null) {
            shaper.begin(ShapeRenderer.ShapeType.Line);
            shaper.setColor(Color.DARK_GRAY);
            shaper.circle(pos.x, pos.y, range.range, 36);
            shaper.end();
        }
    }
}
