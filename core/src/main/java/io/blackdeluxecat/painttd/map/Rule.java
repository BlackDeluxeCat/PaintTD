package io.blackdeluxecat.painttd.map;

import com.badlogic.gdx.utils.*;

import static io.blackdeluxecat.painttd.game.Game.lfps;

/**
 * 世界规则.
 * */
public class Rule{
    public int ticks;

    /**地图总波数*/
    public int waves;
    /**当前波次*/
    public int wave;
    /**使用波次计时器*/
    public boolean waveTimerRun = true;
    /**波次计时器, tick*/
    public float waveTimer = 0;
    public float waveDelay = 10 * lfps;
    /**地图尺寸*/
    public int width, height;
    /**调色盘, 可用颜色*/
    public ColorPalette colorPalette;
    /**地图是否启用编辑器模式*/
    public boolean isEditor = true;
    /**游戏暂停状态*/
    public boolean isPause = false;

    public ObjectMap<String, Object> data = new ObjectMap<>();

    public void putData(String key, Object value){
        data.put(key, value);
    }

    public <T> T getData(String key, T def){
        return (T)data.get(key, def);
    }
}