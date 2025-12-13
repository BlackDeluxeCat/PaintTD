package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.math.*;

public class Vector2V extends LinkableVar{
    public Vector2 cache = new Vector2();

    public Vector2V(boolean cacheValue){
        super(cacheValue);
    }

    @Override
    public void readLink(LinkableVar port){
        if(port instanceof Vector2V parsedPort){
            cache.set(parsedPort.cache);
        }
    }

    @Override
    public boolean canLink(LinkableVar source){
        return source instanceof Vector2V;
    }
}
