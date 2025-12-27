package io.bdc.painttd.game.path.context;

import com.artemis.*;

public interface EntityContext extends BaseContext{
    void set(Entity entity, World world);

    void set(int entityId, World world);

    Entity get();

    default int getId(){
        return get().getId();
    }

    boolean has();
}
