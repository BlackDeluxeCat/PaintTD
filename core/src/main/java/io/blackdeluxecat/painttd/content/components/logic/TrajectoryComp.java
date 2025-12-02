package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;
import io.blackdeluxecat.painttd.content.trajector.*;

public class TrajectoryComp extends CopyableComponent{
    public Tree tree;

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
