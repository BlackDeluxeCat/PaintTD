package io.bdc.painttd.content.trajector;


import com.badlogic.gdx.utils.*;

/**
 * 树上下文接口, 批量注入
 */
public abstract class VarInjector<T>{
    public Array<Injection> injections = new Array<>();

    public void addInjection(Node node){
        Injection injection = new Injection();
        injection.node = node;
        injections.add(injection);
    }

    public void inject(T object){
        float value = get(object);
        for(Injection injection : injections){
        }
    }

    /** 实现获取浮点值, Null情况获取默认值 */
    public abstract float get(@Null T object);

    public static class Injection{
        public Node node;
        public NullHandle handle = NullHandle.setDef;

        public enum NullHandle{
            setDef, skip;
        }
    }
}