package io.bdc.painttd.map;

import com.artemis.*;
import com.badlogic.gdx.utils.*;

import java.util.*;

/**
 * 技术性工具类.
 * 基于坐标的瓦片索引.
 */
public class Map {
    public int width, height;

    //技术性
    /** entity id映射表, 绑定瓦片与实体. */
    public transient ObjectMap<String, int[]> tileToEntity;
    public transient IntIntMap entityToTile;

    /** 勿直接修改 */
    public transient int[] tiles;
    public transient int[] stains;

    transient World world;

    public void create(World world, int width, int height) {
        this.world = world;
        this.width = width;
        this.height = height;
        tiles = new int[width * height];
        entityToTile = new IntIntMap();
        tileToEntity = new ObjectMap<>();

        tiles = getTileToEntityMap("tile");
        stains = getTileToEntityMap("tileStain");
    }

    public int unsafeGet(int x, int y) {
        return tiles[pos(x, y)];
    }

    public int getTile(int x, int y) {
        if (!validPos(x, y)) return -1;
        return unsafeGet(x, y);
    }

    public boolean validPos(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int pos(int x, int y) {
        return x + y * width;
    }

    public int posX(int pos) {
        return pos % width;
    }

    public int posY(int pos) {
        return pos / width;
    }

    public int getTileStain(int x, int y) {
        return stains[pos(x, y)];
    }

    /**
     * 自行持有int[]数组, 结合{@link #pos(int, int)}快速获取实体id
     * 关于随世界重载更新, 可参考{@link io.bdc.painttd.systems.utils.StaticUtils}
     */
    public int[] getTileToEntityMap(String group) {
        if (!tileToEntity.containsKey(group)) {
            var map = new int[width * height];
            tileToEntity.put(group, map);
            Arrays.fill(map, -1);
            return map;
        }
        return tileToEntity.get(group);
    }

    public void putEntity(int e, String group, int x, int y) {
        removeEntity(e, group);
        if (!validPos(x, y)) return;

        var map = getTileToEntityMap(group);
        entityToTile.put(e, pos(x, y));
        map[pos(x, y)] = e;
    }

    /**
     * 仅提供操作方法, 设计上绑定到瓦片的实体不应该remove
     */
    public void removeEntity(int e, String group) {
        int oldPos = entityToTile.remove(e, -1);
        if (oldPos != -1) {
            getTileToEntityMap(group)[oldPos] = -1;
        }
    }

    public int getEntityUnsafe(int x, int y, String group) {
        return getTileToEntityMap(group)[pos(x, y)];
    }

    /**
     * {@link #getTileToEntityMap(String)}获取int[]并持有, 按pos索引性能更佳
     */
    public int getEntity(int x, int y, String group) {
        //lazy 有效性检查
        if (!validPos(x, y)) return -1;
        int e = getEntityUnsafe(x, y, group);
        if (!world.getEntityManager().isActive(e)) return -1;
        return e;
    }
}