package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.PortFloat;

public class SeqProcessor extends Processor{
    public static PortFloat current = new PortFloat("current", 0);
    public static PortFloat repeat = new PortFloat("repeat", 1);
    public static PortFloat repeatCount = new PortFloat("repeatCount", 2);

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
    public void update(float frame, Node node){
        node.state.shift.setZero();

        if(node.children.size <= 0) return;

        int cur = current.asInt(node);

        var child = node.getChild(cur);

        if(child != null && child.complete == Node.NodeState.process){
            child.update(frame);
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
