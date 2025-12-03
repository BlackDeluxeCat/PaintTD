package io.bdc.painttd.content.trajector;

public class SeqProcessor extends Processor{
    public static StateIVar current = new StateIVar("current", 0);

    public SeqProcessor(int maxChildren){
        super(maxChildren, 0, 0, 1);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
    }

    @Override
    public void reset(Node node){
        super.reset(node);
        current.set(node, 0);
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
            current.set(node, cur + 1);
        }
    }
}
