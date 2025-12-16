package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class DamageSplashComp extends CopyableComponent {
    public float damage;
    public float radius;

    public DamageSplashComp() {
    }

    public DamageSplashComp(float damage, float radius) {
        this.damage = damage;
        this.radius = radius;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        DamageSplashComp otherComp = (DamageSplashComp)other;
        this.damage = otherComp.damage;
        this.radius = otherComp.radius;
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
