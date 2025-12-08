package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.StateI;

public class ParallelProcessor extends Processor{
    public static StateI incompleteCount = new StateI("incompleteCount", 0);

    public ParallelProcessor(int maxChildren){
        super(maxChildren, 0, 0, 1);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
    }

    @Override
    public void restart(Node node){
        super.restart(node);
        incompleteCount.setInt(node.children.size, node);
    }

    @Override
    public boolean shouldComplete(Node node){
        return incompleteCount.asInt(node) == 0;
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.setZero();
        int incomplete = 0;
        for(var child : node.children){
            if(child != null && child.complete == Node.NodeState.process){
                child.update(deltaTicks);
                node.state.shift.add(child.state.shift);
                if(child.complete != Node.NodeState.complete) incomplete++;
            }
        }

        incompleteCount.setInt(incomplete, node);
    }
}
