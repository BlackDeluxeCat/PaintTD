package io.bdc.painttd.content.components.marker;

import io.bdc.painttd.content.components.*;

/** 标识一个实体的类型 */
public class EntityTypeComp extends CopyableComponent {
    public String type;

    public EntityTypeComp() {
    }

    public EntityTypeComp(String type) {
        this.type = type;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        this.type = ((EntityTypeComp)other).type;
        return this;
    }

    @Override
    protected void reset() {
    }
}