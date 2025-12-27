package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;
import io.bdc.painttd.game.path.*;

public class NodeGraphExecutorComp extends CopyableComponent {
    public Executor executor = new Executor();

    public NodeGraphExecutorComp() {
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        if (other instanceof NodeGraphExecutorComp otherComp) {
            this.executor.triggerNodeId = otherComp.executor.triggerNodeId;
        }
        return this;
    }

    @Override
    protected void reset() {
        executor.reset();
    }
}
