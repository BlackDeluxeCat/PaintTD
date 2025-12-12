package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;
import io.bdc.painttd.content.trajector.*;

public class TrajectoryComp extends CopyableComponent{
    public Net net = new Net();

    public TrajectoryComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        if(other instanceof TrajectoryComp otherComp){
            net.copy(otherComp.net);
        }
        return this;
    }

    @Override
    protected void reset(){
        net.clear();
    }
}
