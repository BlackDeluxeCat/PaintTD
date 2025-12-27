package io.bdc.painttd.game.path;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.context.*;
import io.bdc.painttd.utils.func.*;

public class Contexts {
    public ObjectMap<Class<? extends BaseContext>, BaseContext> contexts = new ObjectMap<>();

    public <T extends BaseContext> void set(Class<T> clazz, T object) {
        contexts.put(clazz, object);
    }

    public <T extends BaseContext> void set(T object) {
        contexts.put(object.getClass(), object);
    }

    public <T extends BaseContext> void inject(Class<T> clazz, Cons<T> injector) {
        Object obj = contexts.get(clazz);
        if(obj != null) injector.get((T) obj);
    }

    public <T extends BaseContext> T getOrNull(Class<T> clazz) {
        Object obj = contexts.get(clazz);
        if(obj == null) return null;
        return (T) obj;
    }

    public <T extends BaseContext> boolean has(Class<T> clazz) {
        return contexts.containsKey(clazz);
    }

    public void resetAll() {
        for (BaseContext context : contexts.values()) {
            context.reset();
        }
    }

    public void clearAll() {
        contexts.clear();
    }
}
