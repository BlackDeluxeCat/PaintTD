package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class TileComp extends CopyableComponent{
    public boolean isWall = false;

    public TileComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        TileComp tile = (TileComp) other;
        this.isWall = tile.isWall;
        return this;
    }

    @Override
    protected void reset(){
    }
}
