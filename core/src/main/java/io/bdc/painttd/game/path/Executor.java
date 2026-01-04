package io.bdc.painttd.game.path;

import com.badlogic.gdx.utils.*;

/**
 * 节点用户运行时
 * 管理用户独立的运行状态
 */
public class Executor implements Pool.Poolable {
    public int triggerNodeId;
    public float frame;

    public void update(NodeGraph graph, Contexts contexts, float delta) {
        graph.inject(contexts);
        frame += delta;
        graph.calc(frame);
    }

    @Override
    public void reset(){
        triggerNodeId = -1;
        frame = 0;
    }
}
