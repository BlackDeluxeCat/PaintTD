package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.*;

public class TrajectoryComp extends CopyableComponent{
    public TrajectoryTree tree;

    public TrajectoryComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){
        tree.clear();
    }
}
