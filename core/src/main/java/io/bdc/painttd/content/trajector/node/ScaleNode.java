package io.bdc.painttd.content.trajector.node;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

@NodeInfo(
    displayName = "node.scale.name",
    description = "node.scale.description",
    backgroundColor = "#4CAF50",  // 绿色，表示变换节点
    icon = "scale_icon",
    category = "transform",
    inputPorts = {
        @NodeInfo.Port(
            varName = "shiftI",
            color = "#FF9800",  // 橙色
            icon = "input_vector"
        ),
        @NodeInfo.Port(
            varName = "scaleI",
            color = "#E91E63",  // 粉色
            icon = "input_vector"
        )
    },
    outputPorts = {
        @NodeInfo.Port(
            varName = "shiftO",
            color = "#2196F3",  // 蓝色
            icon = "output_vector"
        )
    }
)
public class ScaleNode extends Node{
    public Vector2V scaleI = new Vector2V(true){
        @Override
        public void reset(){
            cache.set(1f, 1f);
        }
    };

    public Vector2V shiftI = new Vector2V(true){
        @Override
        public void reset(){
            cache.set(0f, 0f);
        }
    };

    public Vector2V shiftO = new Vector2V(false);

    @Override
    public void registerVars(){
        inputs.add(shiftI);
        inputs.add(scaleI);
        outputs.add(shiftO);
    }

    @Override
    public boolean calc(float frame){
        scaleI.sync(nodeGraph, frame);
        shiftI.sync(nodeGraph, frame);
        shiftO.cache.set(shiftI.cache).scl(scaleI.cache);
        return true;
    }

    @Override
    public ScaleNode obtain(){
        return pool.obtain();
    }

    private final Pool<ScaleNode> pool = new ReflectionPool<>(ScaleNode.class, 100);

    @Override
    public void free(){
        pool.free(this);
    }

    @Override
    public void reset(){
        scaleI.reset();
        shiftI.reset();
        shiftO.reset();
    }
}
