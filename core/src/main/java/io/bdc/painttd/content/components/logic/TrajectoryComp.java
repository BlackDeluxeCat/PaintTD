package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;
import io.bdc.painttd.content.trajector.*;

public class TrajectoryComp extends CopyableComponent{
    public NodeGraph nodeGraph = new NodeGraph();

    public TrajectoryComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        if(other instanceof TrajectoryComp otherComp){
            nodeGraph.copy(otherComp.nodeGraph);
        }
        return this;
    }

    @Override
    protected void reset(){
        nodeGraph.clear();
    }
}
