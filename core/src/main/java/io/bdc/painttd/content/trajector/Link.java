package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.utils.*;

public class Link implements Pool.Poolable{
    public int source;
    public int sourceOutputPort;
    public int selfInputPort;

    public Link(){
    }

    public Link set(int source, int sourceOutput, int targetInput){
        this.source = source;
        this.sourceOutputPort = sourceOutput;
        this.selfInputPort = targetInput;
        return this;
    }

    @Override
    public void reset(){
        source = sourceOutputPort = selfInputPort = -1;
    }

    public static Pool<Link> pool = Pools.get(Link.class, 10000);

    public static Link obtain(){
        return pool.obtain();
    }
}
