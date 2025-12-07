package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.utils.*;

public abstract class Context<T>{
    public Array<VarInjector<T>> injectors = new Array<>();

    public <A extends VarInjector<T>> A addInjector(A injector){
        injectors.add(injector);
        return injector;
    }

    public void inject(Object object){
        T context = parse(object);
        //注入对象
        for(var injector : injectors){
            injector.inject(context);
        }
    }

    public abstract T parse(Object object);
}
