package io.bdc.painttd.content.trajector.var;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.trajector.*;

/**
 * 一个典型的组合变量，用于操作二维向量
 * 读写分量操作应该通过该组合变量的2个子变量完成
 */
public abstract class Vector2V extends BaseVar implements Var<Vector2>{
    protected Vector2 tmp = new Vector2();

    public Vector2V(String name){
        super(name);
    }

    public void set(float x, float y, Node node){
        tmp.set(x, y);
        set(tmp, node);
    }
}
