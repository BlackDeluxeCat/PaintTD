package io.bdc.painttd.content.trajector;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.utils.*;

public class Context{
    //测试上下文, 暂未TODO实现泛型
    @EntityId public Entity context;
    public Array<VarInjector<Entity>> injectors = new Array<>();

    public void inject(Object object){
        context = parse(object);
        //注入对象
        for(var injector : injectors){
            injector.inject(context);
        }
    }

    public Entity parse(Object object){
        if(object instanceof Entity){
            return (Entity) object;
        }else{
            return null;
        }
    }
}
