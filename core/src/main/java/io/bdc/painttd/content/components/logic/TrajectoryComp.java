package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;
import io.bdc.painttd.content.trajector.*;

public class TrajectoryComp extends CopyableComponent{
    public Tree tree = new Tree();

    public TrajectoryComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        if(other instanceof TrajectoryComp otherComp){
            tree.copy(otherComp.tree);
        }
        return this;
    }

    @Override
    protected void reset(){
        tree.clear();
    }
}
