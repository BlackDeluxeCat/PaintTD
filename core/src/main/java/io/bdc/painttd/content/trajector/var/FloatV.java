package io.bdc.painttd.content.trajector.var;

public class FloatV extends LinkableVar{
    public float cache;

    public FloatV(boolean cacheValue){
        super(cacheValue);
    }

    @Override
    public void syncLink(Linkable port){
        if(port instanceof FloatV parsedPort){
            cache = parsedPort.cache;
        }
    }

    @Override
    public boolean canLink(Linkable source){
        return source instanceof FloatV;
    }
}
