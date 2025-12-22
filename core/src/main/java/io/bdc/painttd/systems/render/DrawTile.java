package io.bdc.painttd.systems.render;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.render.*;

public class DrawTile extends BaseSystem {
    @Wire
    public ComponentMapper<TileComp> tileMapper;

    public Rectangle rect = new Rectangle();

    @Override
    protected void processSystem() {
        Renderer.line.setStroke(0.5f / Renderer.tileSize);
        for (int x = 0; x < Game.map.width; x++) {
            for (int y = 0; y < Game.map.height; y++) {
                int tile = Game.map.getTile(x, y);
                if (tile != -1 && tileMapper.get(tile).isWall) {
                    Renderer.setColor(Color.WHITE);
                    rect.setSize(0.8f).setCenter(x, y);
                    Renderer.line.rect(rect);
                }

                Renderer.setColor(Color.DARK_GRAY);
                rect.setSize(1f).setCenter(x, y);
                Renderer.line.rect(rect);
            }
        }
    }
}
