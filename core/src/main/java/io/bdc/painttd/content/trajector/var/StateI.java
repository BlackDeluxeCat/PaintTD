package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;

public class StateI extends IndexedBaseVar implements VarInt, VarFloat, VarBool{
    public StateI(String name, int index){
        super(name, index);
    }

    @Override
    public void setInt(int value, Node node){
        node.state.ints.set(index, value);
    }

    @Override
    public int asInt(Node node){
        return node.state.ints.get(index);
    }

    @Override
    public void setFloat(float value, Node node){
        setInt((int)value, node);
    }

    @Override
    public float asFloat(Node node){
        return (float)asInt(node);
    }

    @Override
    public void setBool(boolean value, Node node){
        setInt(value ? 1 : 0, node);
    }

    @Override
    public boolean asBool(Node node){
        return asInt(node) != 0;
    }
}
