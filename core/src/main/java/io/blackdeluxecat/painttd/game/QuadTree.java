package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.struct.func.*;

/**
 * 点坐标为实体id映射的{@link io.blackdeluxecat.painttd.content.components.logic.PositionComp}
 *
 * @author BlackDeluxeCat
 */
public class QuadTree{
    protected static Rectangle re = new Rectangle(), r1 = new Rectangle();
    protected static Vector2 v = new Vector2();
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<HitboxComp> hm;
    public final int maxValues;
    public final int maxDepth;

    public QuadTreeNode root;

    protected IntArray innerResults = new IntArray();

    public QuadTree(int maxValue, int maxDepth){
        this.maxValues = maxValue;
        this.maxDepth = maxDepth;
    }

    public QuadTree(){
        this(32, 24);
    }

    public void create(World world, float x, float y, float width, float height){
        clear();
        this.pm = world.getMapper(PositionComp.class);
        this.hm = world.getMapper(HitboxComp.class);
        root.set(this, x, y, width, height, 0);
    }

    public void add(int entityId){
        pos(entityId);
        root.add(entityId, v.x, v.y);
    }

    /**
     * 查询矩形, xy为矩形左下角坐标
     */
    public void queryRect(float x, float y, float width, float height, IntArray result, @Null IntBoolf filter){
        root.query(x, y, width, height, result);
        if(filter != null){
            for(int i = result.size - 1; i >= 0; i--)
                if(!filter.get(result.get(i))) result.removeIndex(i);
        }
    }

    public void queryRect(Rectangle rect, IntArray result, @Null IntBoolf filter){
        queryRect(rect.x, rect.y, rect.width, rect.height, result, filter);
    }

    public void eachRect(float x, float y, float w, float h, @Null IntBoolf filter, Intc cons){
        innerResults.clear();
        queryRect(x, y, w, h, innerResults, filter);
        if(innerResults.isEmpty()) return;
        for(int i = 0; i < innerResults.size; i++){
            cons.get(innerResults.get(i));
        }
    }

    public int closestRect(float x, float y, float w, float h, IntFloatf filter){
        innerResults.clear();
        queryRect(x, y, w, h, innerResults, null);
        if(innerResults.isEmpty()) return -1;
        int closest = -1;
        float closestDist = Float.MAX_VALUE;
        for(int i = 0; i < innerResults.size; i++){
            float dist = filter.get(innerResults.get(i));
            if(dist < closestDist){
                closestDist = dist;
                closest = innerResults.get(i);
            }
        }
        return closest;
    }

    public int closestRect(float x, float y, float w, float h){
        return closestRect(x, y, w, h, (id) -> {
            pos(id);
            return v.sub(x, y).len2();
        });
    }

    /**查询圆心坐标xy, 半径为radius的圆域.
     * 查询碰撞箱中心在圆域内的实体.
     * */
    public void queryCircle(float x, float y, float radius, IntArray result, @Null IntBoolf filter){
        root.query(x - radius, y - radius, radius * 2, radius * 2, result);
        for(int i = result.size - 1; i >= 0; i--){
            pos(result.get(i));
            //比较平方大小来判断距离
            if(v.sub(x, y).len2() > radius * radius) result.removeIndex(i);
            if(filter != null && !filter.get(result.get(i))) result.removeIndex(i);
        }
    }

    public void eachCircle(float x, float y, float radius, @Null IntBoolf filter, Intc cons){
        innerResults.clear();
        queryCircle(x, y, radius, innerResults, filter);
        if(innerResults.isEmpty()) return;
        for(int i = 0; i < innerResults.size; i++){
            cons.get(innerResults.get(i));
        }
    }

    public int closestCircle(float x, float y, float radius, IntFloatf filter){
        innerResults.clear();
        queryCircle(x, y, radius, innerResults, null);
        if(innerResults.isEmpty()) return -1;
        int closest = -1;
        float closestDist = Float.MAX_VALUE;
        for(int i = 0; i < innerResults.size; i++){
            float dist = filter.get(innerResults.get(i));
            if(dist < closestDist){
                closestDist = dist;
                closest = innerResults.get(i);
            }
        }
        return closest;
    }

    public int closestCircle(float x, float y, float radius){
        return closestCircle(x, y, radius, (id) -> {
            pos(id);
            return v.sub(x, y).len2();
        });
    }

    public void clear(){
        if(root != null){
            QuadTreeNode.pool.free(root);
        }
        root = QuadTreeNode.pool.obtain();
    }

    public static class QuadTreeNode implements Pool.Poolable{

        protected static final Pool<QuadTreeNode> pool = new Pool<>(128, 4096){
            protected QuadTreeNode newObject(){
                return new QuadTreeNode();
            }
        };

        protected static QuadTreeNode obtainChild(QuadTree tree, float x, float y, float width, float height, int depth){
            QuadTreeNode child = pool.obtain();
            child.set(tree, x, y, width, height, depth);
            return child;
        }

        public QuadTree tree;
        //left bottom coordinates
        public float width, height, x, y;
        public int depth;
        protected int count;

        public int[] values;
        public QuadTreeNode nw, ne, sw, se;

        public void set(QuadTree tree, float x, float y, float width, float height, int depth){
            this.tree = tree;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.count = 0;
            this.values = new int[tree.maxValues];
        }

        /**
         * 查询矩形
         * @param x 矩形左下角x坐标
         * @param y 矩形左下角y坐标
         */
        public void query(float x, float y, float w, float h, IntArray result){
            //使用矩形重叠粗判断是否查询该节点
            if(!re.set(x, y, w, h).overlaps(r1.set(this.x, this.y, this.width, this.height))) return;

            if(count == -1){
                if(nw != null) nw.query(x, y, w, h, result);
                if(ne != null) ne.query(x, y, w, h, result);
                if(sw != null) sw.query(x, y, w, h, result);
                if(se != null) se.query(x, y, w, h, result);
            }

            if(count > 0){
                for(int i = 0; i < count; i++){
                    if(tree.hitbox(values[i]).overlaps(r1.set(x, y, w, h))){
                        result.add(values[i]);
                    }
                }
            }
        }

        /**
         * 添加一个实体. 检查碰撞箱与区域是否存在重叠.
         */
        public void add(int entity, float valueX, float valueY){
            if(count == -1){
                addToChild(entity, valueX, valueY);
            }else if(count == tree.maxValues){
                if(depth < tree.maxDepth){
                    split();
                    addToChild(entity, valueX, valueY);
                }else{
                    throw new RuntimeException("QuadTree is full");
                }
            }else if(tree.hitbox(entity).overlaps(r1.set(x, y, width, height))){
                values[count++] = entity;
            }
        }

        protected void split(){
            int[] values = this.values;
            for(int i = 0; i < tree.maxValues; i++){
                tree.pos(values[i]);
                addToChild(values[i], v.x, v.y);
            }
            // values isn't nulled because the trees are pooled.
            count = -1;
        }

        private void addToChild(int value, float valueX, float valueY){
            QuadTreeNode child;
            float halfWidth = width / 2, halfHeight = height / 2;
            if(valueX < x + halfWidth){
                if(valueY < y + halfHeight)
                    child = sw != null ? sw : (sw = obtainChild(tree, x, y, halfWidth, halfHeight, depth + 1));
                else
                    child = nw != null ? nw : (nw = obtainChild(tree, x, y + halfHeight, halfWidth, halfHeight, depth + 1));
            }else{
                if(valueY < y + halfHeight)
                    child = se != null ? se : (se = obtainChild(tree, x + halfWidth, y, halfWidth, halfHeight, depth + 1));
                else
                    child = ne != null ? ne : (ne = obtainChild(tree, x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth + 1));
            }
            child.add(value, valueX, valueY);
        }

        @Override
        public void reset(){
            if(count == -1){
                if(nw != null){
                    pool.free(nw);
                    nw = null;
                }
                if(sw != null){
                    pool.free(sw);
                    sw = null;
                }
                if(ne != null){
                    pool.free(ne);
                    ne = null;
                }
                if(se != null){
                    pool.free(se);
                    se = null;
                }
            }
            count = 0;
            values = null;
            tree = null;
        }
    }

    /**获取一个实体的碰撞箱, 储存在{@link #re}*/
    protected Rectangle hitbox(int entity){
        return re.setSize(hm.get(entity).width, hm.get(entity).height).setCenter(pm.get(entity).x, pm.get(entity).y);
    }

    public Rectangle hitbox(int entity, Rectangle out){
        return out.set(hitbox(entity));
    }

    public Vector2 pos(int entity){
        return pm.get(entity).out(v);
    }
}
