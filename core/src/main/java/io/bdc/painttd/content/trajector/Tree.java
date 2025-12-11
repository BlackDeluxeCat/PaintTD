package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

/**
 * 轨迹树, 维护扁平化的节点数组
 * 树结构由{@link Node}{@code .children}表示
 */

public class Tree{
    /** 根节点索引 */
    public int rootIndex;
    public Array<Node> nodes = new Array<>();

    public Vector2 shift = new Vector2();

    /** 上下文主动注入环境数据 */
    public Context<?> context;
    public Array<Trigger> triggers = new Array<>();

    public Tree(){
    }

    public int add(Node node){
        node.tree = this;
        int index = nodes.size;
        nodes.add(node);
        return index;
    }

    public int add(Processor type){
        Node newNode = Node.obtain();
        newNode.setProcessor(type);
        return add(newNode);
    }

    public @Null Node get(int index){
        if(index < 0 || index >= nodes.size) return null;
        return nodes.get(index);
    }

    public int get(Node node){
        return nodes.indexOf(node, true);
    }

    public Node add(Processor type, @Null Node parent){
        Node newNode = Node.obtain();
        newNode.setProcessor(type);
        add(newNode, parent);
        return newNode;
    }

    public void add(Node child, @Null Node parent){
//        if(child == null) return;
//        child.tree = this;
//        nodes.add(child);
//        if(parent == null){
//            rootIndex = nodes.size - 1;
//        }else{
//            //子节点可用的情况下, 添加
//            parent.addChild(child);
//        }
    }

//    /** 从树中移除一个节点, 其子节点转移到其父节点上. */
//    @Deprecated
//    public void remove(Node node){
//        if(node == null) return;
//        int removed = nodes.indexOf(node, true);
//        if(removed == -1) return;
//        nodes.removeIndex(removed);
//        //尝试将所有子节点接到父节点末尾
//        for(var child : node.children){
//            node.parent.addChild(child);
//        }
//        node.free();
//    }

    public void clear(){
        for(var node : nodes){
            node.free();
        }
        nodes.clear();
        rootIndex = 0;
    }

    public void copy(Tree origin){
        clear();
        for(var node : origin.nodes){
            add(node.copy());
        }
        rootIndex = origin.rootIndex;
    }

    /** 关键. 注入对象到所有节点. */
    public void inject(Object object){
        if(nodes == null) return;
        if(nodes.size == 0) return;
        if(rootIndex >= nodes.size) return;

        context.inject(object);
    }

    /** 关键. 更新节点. */
    public void update(float tick){
        if(nodes == null) return;
        if(nodes.size == 0) return;
        if(rootIndex >= nodes.size) return;

        var root = nodes.get(rootIndex);
        root.update(tick);
    }

    /** 触发器. */
    public void fire(int triggerIndex, Node sender){
        triggers.get(triggerIndex).trigger(sender.tree, sender);
    }

    public Vector2 getShift(){
        return shift;
    }
}
