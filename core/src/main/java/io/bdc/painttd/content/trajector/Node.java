package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.var.*;

public abstract class Node implements Pool.Poolable{
    public Array<BaseVar> vars = new Array<>();
    public Array<Linkable> inputs = new Array<>();
    public Array<Linkable> outputs = new Array<>();

    public transient Net net;

    public Node(){
        registerVars();
    }

    public abstract void registerVars();

    public void setNet(Net net){
        this.net = net;
    }

    public abstract void calc(float frame);

    public Linkable getOutput(int index){
        return outputs.get(index);
    }

    public Node obtainCopy(){
        var newNode = obtain();
        this.copyTo(newNode);
        return newNode;
    }

    public void copyTo(Node copy){
    }

    public abstract Node obtain();
    public abstract void free();
}
