package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class TileStainComp extends CopyableComponent{
    public boolean isCore, lastIsCore;

    public TileStainComp(){}

    public TileStainComp(boolean isCore){
        this.isCore = isCore;
        lastIsCore = !isCore;
    }

    @Override
    public TileStainComp copy(CopyableComponent other){
        TileStainComp o = (TileStainComp) other;
        this.isCore = o.isCore;
        this.lastIsCore = o.lastIsCore;
        return this;
    }

    @Override
    protected void reset(){
        isCore = false;
        lastIsCore = false;
    }
}
