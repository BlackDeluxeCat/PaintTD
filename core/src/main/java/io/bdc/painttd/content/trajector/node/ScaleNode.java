package io.bdc.painttd.content.trajector.node;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

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
