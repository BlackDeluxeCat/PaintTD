package io.bdc.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.components.logic.*;

import java.util.*;

/**
 * 自动化管理地图瓦片和实体的关联, 提供获取实体的快速方法
 * <p>注册自定义映射表: 在任意地方实现{@link EntitySubscription}并在监听器中使用{@code putEntity}和{@code removeEntity}绑定实体</p>
 * <p>使用自定义映射表:
 */
@Wire
public class MapManager extends BaseSystem {
    public int width, height;
    public int outOfBoundSize = 10;

    /** entity id映射表, 绑定瓦片与实体. */
    public ObjectMap<String, IntBag> tileToEntity = new ObjectMap<>();
    public IntIntMap entityToTile = new IntIntMap();

    /** 勿直接修改 */
    public IntBag tiles;
    public IntBag stains;

    public boolean shouldRebindTile;
    @All(value = TileComp.class)
    public EntitySubscription tilesSubscription;
    @All(value = TileStainComp.class)
    public EntitySubscription stainsSubscription;

    public ComponentMapper<PositionComp> positionMapper;

    @Override
    protected void initialize() {
        super.initialize();
        tilesSubscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
            @Override
            public void inserted(IntBag entities) {
            }

            @Override
            public void removed(IntBag entities) {
                for (int i = 0, s = entities.size(); s > i; i++) {
                    removeEntity(entities.get(i), "tile");
                }
            }
        });

        stainsSubscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
            @Override
            public void inserted(IntBag entities) {
            }

            @Override
            public void removed(IntBag entities) {
                for (int i = 0, s = entities.size(); s > i; i++) {
                    removeEntity(entities.get(i), "tileStain");
                }
            }
        });

    }

    public void createMap(int width, int height) {
        this.width = width;
        this.height = height;
        entityToTile.clear();
        tileToEntity.clear();

        tiles = getTileToEntityMap("tile");
        stains = getTileToEntityMap("tileStain");
    }

    public void rebindTileAndStains() {
        IntBag ids = tilesSubscription.getEntities();
        for (int i = 0, s = ids.size(); s > i; i++) {
            int e = tilesSubscription.getEntities().get(i);
            var pos = positionMapper.get(e);
            putEntity(e, "tile", pos.tileX(), pos.tileY());
        }

        ids = stainsSubscription.getEntities();
        for (int i = 0, s = ids.size(); s > i; i++) {
            int e = stainsSubscription.getEntities().get(i);
            var pos = positionMapper.get(e);
            putEntity(e, "tileStain", pos.tileX(), pos.tileY());
        }
    }

    public void queryRect(IntBag bag, int x, int y, int w, int h, IntArray out) {
        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                if (validPos(x + dx, y + dy)) {
                    int tile = bag.get(pos(x + dx, y + dy));
                    if (tile != -1) out.add(tile);
                }
            }
        }
    }

    public void queryRectCenter(IntBag bag, int x, int y, int w, int h, IntArray out) {
        queryRect(bag, x - w / 2, y - h / 2, w, h, out);
    }

    public void queryCircle(IntBag bag, int x, int y, int r, IntArray out) {
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                if (validPos(x + dx, y + dy) && dx * dx + dy * dy <= r * r) {
                    int tile = bag.get(pos(x + dx, y + dy));
                    if (tile != -1) out.add(tile);
                }
            }
        }
    }

    public int unsafeGet(int x, int y) {
        return tiles.get(pos(x, y));
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
        return stains.get(pos(x, y));
    }

    /**
     * 自行持有数组, 结合{@link #pos(int, int)}快速获取实体id
     * 关于随世界重载更新, 可参考{@link io.bdc.painttd.systems.utils.StaticUtils}
     */
    public IntBag getTileToEntityMap(String group) {
        if (!tileToEntity.containsKey(group)) {
            var map = new IntBag(width * height);
            Arrays.fill(map.getData(), -1);
            tileToEntity.put(group, map);
            return map;
        }
        return tileToEntity.get(group);
    }

    public void putEntity(int e, String group, int x, int y) {
        removeEntity(e, group);
        if (!validPos(x, y)) return;

        var map = getTileToEntityMap(group);
        entityToTile.put(e, pos(x, y));
        map.set(pos(x, y), e);
    }

    /** 绑定到瓦片的实体仅在游戏结束后remove. 除非你充分知道在做什么, 不要在实体订阅监听器以外的地方调用!!! */
    public void removeEntity(int e, String group) {
        int oldPos = entityToTile.remove(e, -1);
        if (oldPos != -1) {
            getTileToEntityMap(group).set(oldPos, -1);
        }
    }

    public int getEntityUnsafe(int x, int y, String group) {
        return getTileToEntityMap(group).get(pos(x, y));
    }

    /** {@link #getTileToEntityMap(String)}获取int[]并持有, 按pos索引性能更佳 */
    public int getEntity(int x, int y, String group) {
        //lazy 有效性检查
        if (!validPos(x, y)) return -1;
        int e = getEntityUnsafe(x, y, group);
        if (!world.getEntityManager().isActive(e)) return -1;
        return e;
    }

    @Override
    protected void processSystem() {
        if (shouldRebindTile) {
            rebindTileAndStains();
            shouldRebindTile = false;
        }
    }
}
