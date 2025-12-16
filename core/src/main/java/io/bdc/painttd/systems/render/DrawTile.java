package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.game.*;

import static io.bdc.painttd.Core.*;

public class DrawTile extends BaseSystem {
    @Wire
    public ComponentMapper<TileComp> tileMapper;

    @Override
    protected void processSystem() {
        shaper.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < Game.map.width; x++) {
            for (int y = 0; y < Game.map.height; y++) {
                int tile = Game.map.getTile(x, y);
                if (tile != -1 && tileMapper.get(tile).isWall) {
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x - 0.4f, y - 0.4f, 0.8f, 0.8f);
                }

                shaper.setColor(Color.DARK_GRAY);
                shaper.rect(x - 0.5f, y - 0.5f, 1, 1);

            }
        }
        shaper.end();
    }
}
