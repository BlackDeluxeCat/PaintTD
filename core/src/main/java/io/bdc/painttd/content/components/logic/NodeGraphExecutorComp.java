package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;
import io.bdc.painttd.game.path.*;

public class NodeGraphExecutorComp extends CopyableComponent {
    //同类实体共用一张图
    public NodeGraph graph;
    public Executor executor = new Executor();

    public NodeGraphExecutorComp() {
    }

    public NodeGraphExecutorComp(NodeGraph graph) {
        this.graph = graph;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        if (other instanceof NodeGraphExecutorComp otherComp) {
            this.graph = otherComp.graph;
            this.executor.triggerNodeId = otherComp.executor.triggerNodeId;
        }
        return this;
    }

    @Override
    protected void reset() {
        executor.reset();
    }
}
