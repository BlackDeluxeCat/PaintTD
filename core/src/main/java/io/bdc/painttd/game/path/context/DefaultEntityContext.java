package io.bdc.painttd.game.path.context;

import com.artemis.*;

public class DefaultEntityContext implements EntityContext{
    public World world;
    public int entityId;
    public Entity entity;
    @Override
    public void set(Entity entity, World world) {
        this.world = world;
        this.entity = entity;
        this.entityId = entity.getId();
    }

    @Override
    public void set(int entityId, World world) {
        this.world = world;
        this.entity = world.getEntity(entityId);
        this.entityId = entityId;
    }

    @Override
    public Entity get() {
        return entity;
    }

    @Override
    public int getId() {
        return entityId;
    }

    @Override
    public boolean has() {
        return entity != null;
    }

    @Override
    public void reset() {
        world = null;
        entity = null;
        entityId = -1;
    }
}
