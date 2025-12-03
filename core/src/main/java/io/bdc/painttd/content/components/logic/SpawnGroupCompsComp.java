package io.bdc.painttd.content.components.logic;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.*;
import io.bdc.painttd.content.components.*;
import io.bdc.painttd.utils.*;

public class SpawnGroupCompsComp extends CopyableComponent{
    public static ObjectSet<Class<?>> availableComps = new ObjectSet<>();

    static{
        availableComps.addAll(HealthRegenComp.class, ArmorComp.class);
    }

    public Array<CopyableComponent> comps = new Array<>();

    public SpawnGroupCompsComp(){
    }

    public boolean add(CopyableComponent comp){
        if(has(comp.getClass()) != null){
            return false;
        }

        comps.add(comp);
        return true;
    }

    public boolean remove(CopyableComponent comp){
        return comps.removeValue(comp, true);
    }

    public boolean remove(Class<? extends CopyableComponent> clazz){
        return comps.removeValue(IterateUtils.first(comps, c -> c.getClass().equals(clazz)), true);
    }

    public @Null <T extends CopyableComponent> CopyableComponent has(Class<T> clazz){
        return IterateUtils.first(comps, c -> c.getClass().equals(clazz));
    }

    @Override
    protected void reset(){
        comps.clear();
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        comps.clear();
        try{
            for(CopyableComponent c : ((SpawnGroupCompsComp)other).comps){
                comps.add(c.copy(ClassReflection.newInstance(c.getClass())));
            }
        }catch(ReflectionException e){
            throw new RuntimeException(e);
        }

        return this;
    }
}
