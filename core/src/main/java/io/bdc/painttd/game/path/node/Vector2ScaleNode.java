package io.bdc.painttd.game.path.node;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.var.*;

@NodeInfo(
    nodeType = "vector2Scale",
    displayName = "name",
    description = "description",
    backgroundColor = "#4CAF50",  // 绿色，表示变换节点
    icon = "scale_icon",
    category = "transform",
    inputPorts = {
        @NodeInfo.Port(
            fieldName = "shift",
            color = "#FF9800",  // 橙色
            icon = "input_vector"
        ),
        @NodeInfo.Port(
            fieldName = "scale",
            color = "#E91E63",  // 粉色
            icon = "input_vector"
        )
    },
    outputPorts = {
        @NodeInfo.Port(
            fieldName = "shift",
            color = "#2196F3",  // 蓝色
            icon = "output_vector"
        )
    }
)
public class Vector2ScaleNode extends Node {
    public Vector2V scaleI = new Vector2V(true) {
        @Override
        public void reset() {
            cache.set(1f, 1f);
        }
    };

    public Vector2V shiftI = new Vector2V(true) {
        @Override
        public void reset() {
            cache.set(0f, 0f);
        }
    };

    public Vector2V shiftO = new Vector2V(false);

    @Override
    public void registerVars() {
        inputs.add(shiftI);
        inputs.add(scaleI);
        outputs.add(shiftO);
    }

    @Override
    public boolean calc(float frame) {
        scaleI.sync(nodeGraph, frame);
        shiftI.sync(nodeGraph, frame);
        shiftO.cache.set(shiftI.cache).scl(scaleI.cache);
        return true;
    }

    @Override
    public Vector2ScaleNode obtain() {
        return pool.obtain();
    }

    private final Pool<Vector2ScaleNode> pool = new ReflectionPool<>(Vector2ScaleNode.class, 100);

    @Override
    public void free() {
        pool.free(this);
    }

    @Override
    public void reset() {
        scaleI.reset();
        shiftI.reset();
        shiftO.reset();
    }
}
