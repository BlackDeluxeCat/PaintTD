package io.bdc.painttd.content.components.logic.physics;

import io.bdc.painttd.content.components.*;

public class HitboxComp extends CopyableComponent {
    public float x = 1, y = 1;
    public float z = 1;
    public float scale = 1;

    public HitboxComp() {
    }

    public HitboxComp(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public HitboxComp(float size) {
        this.x = size;
        this.y = size;
    }

    public HitboxComp z(float z) {
        this.z = z;
        return this;
    }

    public float x() {
        return x * scale;
    }

    public float y() {
        return y * scale;
    }

    //as a 2.5d game, it is usually useless
    public float z() {
        return z * scale;
    }

    @Override
    public HitboxComp copy(CopyableComponent other) {
        HitboxComp hitboxComp = (HitboxComp)other;
        x = hitboxComp.x;
        y = hitboxComp.y;
        z = hitboxComp.z;
        scale = hitboxComp.scale;
        return this;
    }

    @Override
    protected void reset() {
        x = 1;
        y = 1;
        z = 0;
        scale = 1;
    }

    @Override
    public void refill(CopyableComponent def) {
        HitboxComp hitboxComp = (HitboxComp)def;
        x = hitboxComp.x;
        y = hitboxComp.y;
    }
}
