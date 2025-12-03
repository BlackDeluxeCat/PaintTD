package io.bdc.painttd.content.trajector;

public class ParallelProcessor extends Processor{
    public static StateIVar incompleteCount = new StateIVar("incompleteCount", 0);

    public ParallelProcessor(int maxChildren){
        super(maxChildren, 0, 0, 1);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
    }

    @Override
    public void reset(Node node){
        super.reset(node);
        incompleteCount.set(node, node.children.size);
    }

    @Override
    public boolean shouldComplete(Node node){
        return incompleteCount.get(node) == 0;
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

        incompleteCount.set(node, incomplete);
    }
}
