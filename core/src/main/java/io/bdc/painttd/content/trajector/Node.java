package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.var.*;

public abstract class Node implements Pool.Poolable{
    public Array<BaseVar> vars = new Array<>();
    public Array<LinkableVar> inputs = new Array<>();
    public Array<LinkableVar> outputs = new Array<>();

    public transient Net net;

    public Node(){
        registerVars();
    }

    public abstract void registerVars();

    public void setNet(Net net){
        this.net = net;
    }

    public abstract void calc(float frame);

    /** 下游请求sync的output端口提供者, 先使用帧更新计算, 再返回端口. 可以在这里进行端口转发和帧重映射. */
    public @Null LinkableVar getSyncOutput(float frame, int targetOutputPort){
        calc(frame);
        return getOutput(targetOutputPort);
    }

    public LinkableVar getOutput(int index){
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
