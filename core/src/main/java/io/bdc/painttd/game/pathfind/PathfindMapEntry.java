package io.bdc.painttd.game.pathfind;

import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.game.Game;

public interface PathfindMapEntry{
    default boolean isValid(int x, int y){
        int tile = Game.map.getTile(x, y);
        return tile != -1;
    }

    default float cost(int x, int y){
        if(!isValid(x, y)) return 114514f;
        int e = Game.map.getTileStain(x, y);
        if(e != -1 && Game.utils.tileStainMapper.get(e).isCore && Game.utils.healthMapper.get(e).health > 0){
            return 0;
        }
        int tile = Game.map.getTile(x, y);
        TileComp tc = Game.utils.tileMapper.get(tile);
        return tc.isWall ? 114514f : 1f;
    }
}
