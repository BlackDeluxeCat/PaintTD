package io.bdc.painttd.content.trajector;


import com.badlogic.gdx.utils.*;

/**
 * 树上下文接口, 批量注入
 */
public abstract class VarInjector<T>{
    public Array<Injection> injections = new Array<>();

    public void addInjection(Node node, Processor.Var var){
        Injection injection = new Injection();
        injection.node = node;
        injection.var = var;
        injections.add(injection);
    }

    public void inject(T object){
        float value = get(object);
        for(Injection injection : injections){
            if(injection.var instanceof Processor.FloatVar fv){
                //如果对象有效, 注入值. 如果对象无效且注入处理为setDef, 注入get到的默认值
                if(object != null || injection.handle == Injection.NullHandle.setDef){
                    fv.set(injection.node, value);
                }
            }
        }
    }

    /** 实现获取浮点值, Null情况获取默认值 */
    public abstract float get(@Null T object);

    public static class Injection{
        public Node node;
        public Processor.Var var;
        public NullHandle handle = NullHandle.setDef;

        public enum NullHandle{
            setDef, skip;
        }
    }
}