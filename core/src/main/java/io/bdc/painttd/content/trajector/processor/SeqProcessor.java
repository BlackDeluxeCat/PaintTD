package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.StateI;
import io.bdc.painttd.content.trajector.var.ParamF;

public class SeqProcessor extends Processor{
    public static StateI current = new StateI("current", 0);
    public static ParamF repeat = new ParamF("repeat", 0);
    public static StateI repeatCount = new StateI("repeatCount", 1);

    public SeqProcessor(int maxChildren){
        super(maxChildren, 1, 0, 2);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
    }

    @Override
    public void restart(Node node){
        super.restart(node);
        current.setInt(0, node);
        repeatCount.setInt(0, node);
        for(var c : node.children){
            c.processor.restart(c);
        }
    }

    @Override
    public boolean shouldComplete(Node node){
        int cur = current.asInt(node);
        return node.children.size <= 0 || cur >= node.children.size;
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.setZero();

        if(node.children.size <= 0) return;

        int cur = current.asInt(node);

        var child = node.getChild(cur);

        if(child != null && child.complete == Node.NodeState.process){
            child.update(deltaTicks);
            node.state.shift.set(child.state.shift);
        }

        if(child == null || child.complete == Node.NodeState.complete){
            cur++;

            //重复时节点的重置
            if(cur >= node.children.size){
                int max = repeat.asInt(node), count = repeatCount.asInt(node);
                count++;
                if(count < max){
                    repeatCount.setInt(count, node);
                    cur = 0;

                    for(var c : node.children){
                        c.processor.restart(c);
                    }
                }
            }

            current.setInt(cur, node);
        }
    }
}
