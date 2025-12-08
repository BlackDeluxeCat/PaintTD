package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;

public class StateF extends IndexedBaseVar implements VarFloat, VarInt, VarBool{
    public StateF(String name, int index){
        super(name, index);
    }

    @Override
    public void setFloat(float value, Node node){
        node.state.floats.set(index, value);
    }

    @Override
    public float asFloat(Node node){
        return node.state.floats.get(index);
    }

    @Override
    public void setInt(int value, Node node){
        setFloat((float)value, node);
    }

    @Override
    public int asInt(Node node){
        return (int)asFloat(node);
    }

    @Override
    public void setBool(boolean value, Node node){
        setFloat(value ? 1f : 0f, node);
    }

    @Override
    public boolean asBool(Node node){
        return asFloat(node) != 0f;
    }
}
