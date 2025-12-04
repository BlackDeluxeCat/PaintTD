package io.bdc.painttd.content.trajector.processor;

import io.bdc.painttd.content.trajector.*;

public class SeqProcessor extends Processor{
    public static StateIVar current = new StateIVar("current", 0);
    public static ParamVar repeat = new ParamVar("repeat", 0);
    public static StateIVar repeatCount = new StateIVar("repeatCount", 1);

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
        current.set(node, 0);
        repeatCount.set(node, 0);
        for(var c : node.children){
            c.processor.restart(c);
        }
    }

    @Override
    public boolean shouldComplete(Node node){
        int cur = current.get(node);
        return node.children.size <= 0 || cur >= node.children.size;
    }

    @Override
    public void update(float deltaTicks, Node node){
        node.state.shift.setZero();

        if(node.children.size <= 0) return;

        int cur = current.get(node);

        var child = node.getChild(cur);

        if(child != null && child.complete == Node.NodeState.process){
            child.update(deltaTicks);
            node.state.shift.set(child.state.shift);
        }

        if(child == null || child.complete == Node.NodeState.complete){
            cur++;

            //重复时节点的重置
            if(cur >= node.children.size){
                int max = (int)repeat.get(node), count = repeatCount.get(node);
                count++;
                if(count < max){
                    repeatCount.set(node, count);
                    cur = 0;

                    for(var c : node.children){
                        c.processor.restart(c);
                    }
                }
            }

            current.set(node, cur);
        }
    }
}
