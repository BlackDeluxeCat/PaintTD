package io.blackdeluxecat.painttd.game.pathfind;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.map.Map;

import java.util.*;

import static io.blackdeluxecat.painttd.game.Game.groups;
import static io.blackdeluxecat.painttd.game.Game.world;

public class FlowField{
    public Map map;

    public Node[][] nodes;

    public PathfindMapEntry entry = new PathfindMapEntry(){};

    private final PriorityQueue<Node> openList;

    private final int[] d4x = {1, 0, -1, 0};
    private final int[] d4y = {0, 1, 0, -1};


    public FlowField(Map map){
        this.map = map;
        nodes = new Node[map.width][map.height];
        openList = new PriorityQueue<>(comparator);
    }

    /**流场重新生成*/
    public void rebuild(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                if(nodes[x][y] != null) Node.pool.free(nodes[x][y]);
            }
        }

        ComponentMapper<TileStainComp> tileStainMapper = world.getMapper(TileStainComp.class);
        ComponentMapper<HealthComp> hp = world.getMapper(HealthComp.class);

        IntArray toDirty = new IntArray();

        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                nodes[x][y] = Node.obtain();
                nodes[x][y].x = x;
                nodes[x][y].y = y;
                nodes[x][y].cost = entry.cost(x, y);
                nodes[x][y].stackCost = Float.MAX_VALUE;

                int e = map.getEntityUnsafe(x, y, "tileStain");
                if(e != -1 && tileStainMapper.get(e).isCore && hp.get(e).health > 0){
                    nodes[x][y].stackCost = 0;
                    toDirty.add(map.pos(x, y));
                }
            }
        }

        for(int i = 0; i < toDirty.size; i++){
            dirtyNeighbor(toDirty.get(i) % map.width, toDirty.get(i) / map.width);
        }

        //启动扩散
        update();
    }

    /**
     * 扩散. 逐一更新脏化节点, 如果积分更新, 就将所有邻居脏化
     * 父节点应该不会被循环脏化
     */
    public void update(){
        boolean needFlow = false;
        while(!openList.isEmpty()){
            Node current = openList.poll();
            current.cost = entry.cost(current.x, current.y);

            if(updateNode(current)){
                dirtyNeighbor(current.x, current.y);
                needFlow = true;
            }
        }

        if(needFlow) flow();
    }

    public boolean updateNode(Node node){
        boolean updated = false;

        for(int i = 0; i < 4; i++){
            int nx = node.x + d4x[i];
            int ny = node.y + d4y[i];
            if(!entry.isValid(nx, ny)) continue;

            Node neighbor = nodes[nx][ny];
            float newCost = neighbor.stackCost + node.cost;

            if(newCost < node.stackCost){
                node.stackCost = newCost;
                node.parent = neighbor;
                updated = true;
            }
        }

        return updated;
    }

    public void dirtyNeighbor(int x, int y){
        for(int i = 0; i < 4; i++){
            int nx = x + d4x[i];
            int ny = y + d4y[i];
            if(!entry.isValid(nx, ny)) continue;
            openList.add(nodes[nx][ny]);
        }
    }

    /**标记变更的节点及其子节点为脏
     * 在循环中, 脏邻居通常会更新积分, 从而传播更新. 如果遇到了不更新的邻居, 自然停止传播.
     */
    public void change(int changedX, int changedY){
        Node changed = nodes[changedX][changedY];
        if(!openList.contains(changed)) openList.add(changed);
        changed.stackCost = Float.MAX_VALUE;
        for(int i = 0; i < 4; i++){
            int nx = changedX + d4x[i];
            int ny = changedY + d4y[i];
            if(!entry.isValid(nx, ny)) continue;
            Node neighbor = nodes[nx][ny];
            if(neighbor.parent == changed && !openList.contains(neighbor)) change(neighbor.x, neighbor.y);  //递归脏化
        }
    }

    /**生成流场*/
    public void flow(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                Node node = nodes[x][y];
                if(node.parent == null) continue;
                node.direction.set(node.parent.x - x, node.parent.y - y).nor();
            }
        }
    }

    public Vector2 getDirection(int x, int y, Vector2 out){
        return out.set(nodes[x][y].direction);
    }

    protected static Comparator<Node> comparator = new Comparator<Node>(){
        @Override
        public int compare(Node o1, Node o2){
            return Float.compare(o1.stackCost, o2.stackCost);
        }
    };

    public static class Node implements Pool.Poolable{
        public static Pool<Node> pool = new Pool<>(){
            @Override
            protected Node newObject(){
                return new Node();
            }
        };

        public static Node obtain(){
            return pool.obtain();
        }

        public int x, y;
        public float cost, stackCost;
        public Node parent;
        public Vector2 direction = new Vector2();

        @Override
        public void reset(){
            x = y = 0;
            cost = stackCost = 0;
            parent = null;
        }
    }
}
