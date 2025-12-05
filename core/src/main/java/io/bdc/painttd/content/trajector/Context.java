package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.utils.*;

public abstract class Context<T>{
    transient public T context;
    public Array<VarInjector<T>> injectors = new Array<>();

    public void addInjector(VarInjector<T> injector){
        injectors.add(injector);
    }

    public void inject(Object object){
        context = parse(object);
        //注入对象
        for(var injector : injectors){
            injector.inject(context);
        }
    }

    public abstract T parse(Object object);
}
