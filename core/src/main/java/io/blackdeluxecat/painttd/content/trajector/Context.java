package io.blackdeluxecat.painttd.content.trajector;


import com.badlogic.gdx.utils.*;

/**
 * 树上下文, 批量注入
 */
public abstract class Context{
    public Array<Injection> injections = new Array<>();

    public abstract float get();

    public void addInjection(Node node, Processor.Var var){
        Injection injection = new Injection();
        injection.node = node;
        injection.var = var;
        injections.add(injection);
    }

    public void process(){
        float value = get();
        for(Injection injection : injections){
            if(injection.var instanceof Processor.FloatVar fv){
                fv.set(injection.node, value);
            }
        }
    }

    public static class Injection{
        public Node node;
        public Processor.Var var;
    }
}