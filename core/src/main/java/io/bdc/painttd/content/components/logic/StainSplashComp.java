package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class StainSplashComp extends CopyableComponent {
    public int radius;
    public float damage = 1;

    public StainSplashComp() {
    }

    public StainSplashComp(int radius) {
        this.radius = radius;
    }

    @Override
    public StainSplashComp copy(CopyableComponent other) {
        StainSplashComp otherComp = (StainSplashComp)other;
        this.radius = otherComp.radius;
        this.damage = otherComp.damage;
        return this;
    }

    @Override
    protected void reset() {
        this.radius = 0;
        this.damage = 1;
    }
}
