package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.render.*;

import static io.bdc.painttd.game.Game.*;

@Wire
public class DrawTileStain extends BaseSystem {
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TileStainComp> tileStainMapper;

    @Override
    protected void processSystem() {
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                var e = map.getTileStain(x, y);
                if (e == -1) continue;
                var health = healthMapper.get(e);
                var stain = tileStainMapper.get(e);

                if (health.health > 0) {
                    Renderer.setColor(rules.colorPalette.getColor(Vars.c1, MathUtils.ceil(health.health) - 1));
                    Renderer.fill.rect(x - 0.5f, y - 0.5f, 1, 1);
                }

                if (stain.isCore) {
                    Renderer.setColor(Color.WHITE);
                    Renderer.fill.circle(x, y, 0.3f, 12);
                }
            }
        }
    }
}
