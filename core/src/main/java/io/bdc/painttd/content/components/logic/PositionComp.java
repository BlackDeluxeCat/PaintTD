package io.bdc.painttd.content.components.logic;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.components.*;

public class PositionComp extends CopyableComponent {
    public float x, y;
    public float z;

    public PositionComp() {
    }

    public PositionComp(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionComp(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionComp z(float z) {
        this.z = z;
        return this;
    }

    public int tileX() {
        return Math.round(x);
    }

    public int tileY() {
        return Math.round(y);
    }

    public Vector2 out(Vector2 out) {
        return out.set(x, y);
    }

    @Override
    protected void reset() {
        x = 0;
        y = 0;
        z = 0;
    }

    @Override
    public PositionComp copy(CopyableComponent other) {
        PositionComp positionComp = (PositionComp)other;
        this.x = positionComp.x;
        this.y = positionComp.y;
        this.z = positionComp.z;
        return this;
    }
}
