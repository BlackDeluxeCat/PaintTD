package io.bdc.painttd.content.trajector;

import com.artemis.*;

public class EntityContext extends Context<Entity>{
    @Override
    public Entity parse(Object object){
        if(object instanceof Entity e){
            return e;
        }
        return null;
    }
}
