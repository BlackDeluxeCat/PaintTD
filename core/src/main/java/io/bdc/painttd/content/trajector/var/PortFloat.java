package io.bdc.painttd.content.trajector.var;

import io.bdc.painttd.content.trajector.*;

public class PortFloat extends IndexedBaseVar implements VarFloat, AsBool, AsInt{
    public PortFloat(String name, int index){
        super(name, index);
    }

    @Override
    public void setFloat(float value, Node node){
        node.param.set(index, value);
    }

    @Override
    public float asFloat(Node node){
        return node.param.get(index);
    }

    @Override
    public void setBool(boolean value, Node node){
        node.param.set(index, value ? 1 : 0);
    }

    @Override
    public boolean asBool(Node node){
        return node.param.get(index) != 0;
    }

    @Override
    public void setInt(int value, Node node){
        node.param.set(index, value);
    }

    @Override
    public int asInt(Node node){
        return (int)node.param.get(index);
    }
}
