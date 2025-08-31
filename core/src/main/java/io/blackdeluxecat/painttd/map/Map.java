package io.blackdeluxecat.painttd.map;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.*;

import java.util.*;

/**
 * 基于坐标的瓦片索引.
 * */
public class Map{
    public int width, height;
    public Tile[] tiles;

    /**使用group name - entity id调取特定实体/瓦片. 请勿随意修改实体组件组合*/
    public ObjectMap<String, int[]> tileToEntity;
    public IntIntMap entityToTile;

    public ColorPalette colorPalette;

    World world;

    public void create(World world, int width, int height, ColorPalette colorPalette){
        this.world = world;
        this.width = width;
        this.height = height;
        tiles = new Tile[width * height];
        entityToTile = new IntIntMap();
        tileToEntity = new ObjectMap<>();

        this.colorPalette = colorPalette;

        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new Tile(i % width, i / width);
        }
    }

    public Tile unsafeGet(int x, int y){
        return tiles[x + y * width];
    }

    public Tile get(int x, int y){
        if(!validPos(x, y)) return null;
        return unsafeGet(x, y);
    }

    public Tile get(int pos){
        return get(pos % width, pos / width);
    }

    public boolean validPos(int x, int y){
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int pos(int x, int y){
        return x + y * width;
    }
    
    public int[] getTileToEntityMap(String group){
        if(!tileToEntity.containsKey(group)){
            var map = new int[width * height];
            tileToEntity.put(group, map);
            Arrays.fill(map, -1);
            return map;
        }
        return tileToEntity.get(group);
    }

    public void putEntity(int e, String group, int x, int y){
        removeEntity(e, group);
        if(!validPos(x, y)) return;

        var map = getTileToEntityMap(group);
        entityToTile.put(e, pos(x, y));
        map[pos(x, y)] = e;
    }

    public void removeEntity(int e, String group){
        int oldPos = entityToTile.remove(e, -1);
        if(oldPos != -1){
            getTileToEntityMap(group)[oldPos] = -1;
        }
    }

    public int getEntityUnsafe(int x, int y, String group){
        return getTileToEntityMap(group)[pos(x, y)];
    }

    /**高性能get方案是{@link #getTileToEntityMap(String)}获取数组引用并按pos索引*/
    public int getEntity(int x, int y, String group){
        //lazy 有效性检查
        if(!validPos(x, y)) return -1;
        int e = getEntityUnsafe(x, y, group);
        if(!world.getEntityManager().isActive(e)) return -1;
        return e;
    }
}