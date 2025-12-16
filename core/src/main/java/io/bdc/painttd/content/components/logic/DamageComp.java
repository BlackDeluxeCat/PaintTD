package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class DamageComp extends CopyableComponent {
    public float damage;

    public DamageComp() {
    }

    public DamageComp(float damage) {
        this.damage = damage;
    }

    @Override
    public DamageComp copy(CopyableComponent other) {
        DamageComp damageComp = (DamageComp)other;
        this.damage = damageComp.damage;
        return this;
    }

    @Override
    protected void reset() {
    }

    @Override
    public void refill(CopyableComponent def) {
        copy(def);
    }
}
