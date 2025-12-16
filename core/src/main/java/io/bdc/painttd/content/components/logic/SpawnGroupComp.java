package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class SpawnGroupComp extends CopyableComponent {
    public int waveStart = 1;
    public int waveDelta = 1;

    public float amtStart = 1;
    public float amtDelta = 1;
    public int amtMax = 20;

    public float healthStart = 10;
    public float healthDelta = 2;

    /** 生成单位间隔, tick */
    public int spawnDelta = 30;

    //波次生成中状态
    public float amt;
    public float health;

    public void pull(int wave) {
        if (Math.floorMod(wave - waveStart, waveDelta) == 0) {
            int countSpawned = (wave - waveStart) / waveDelta;
            amt = amtStart + amtDelta * countSpawned;
            health = healthStart + healthDelta * countSpawned;
        } else {
            amt = 0;
        }
    }

    @Override
    public CopyableComponent copy(CopyableComponent other) {
        SpawnGroupComp o = (SpawnGroupComp)other;
        waveStart = o.waveStart;
        waveDelta = o.waveDelta;
        amtStart = o.amtStart;
        amtDelta = o.amtDelta;
        amtMax = o.amtMax;
        spawnDelta = o.spawnDelta;
        healthStart = o.healthStart;
        healthDelta = o.healthDelta;
        return this;
    }

    @Override
    protected void reset() {
        amt = 0;
        health = 0;
    }
}
