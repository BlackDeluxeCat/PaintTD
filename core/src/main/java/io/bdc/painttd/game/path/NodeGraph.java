package io.bdc.painttd.game.path;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class NodeGraph {
    public int rootIndex;
    public Array<Node> nodes = new Array<>();
    public Vector2 output = new Vector2();
    //TODO扩展单一output为选择Trigger触发不同功能

    public float frame;

    public NodeGraph() {
    }

    public int add(Node node) {
        int index = nodes.size;
        nodes.add(node);
        node.setNodeGraph(this);
        return index;
    }

    public @Null Node get(int index) {
        if (index < 0 || index >= nodes.size) return null;
        return nodes.get(index);
    }

    public int get(Node node) {
        return nodes.indexOf(node, true);
    }

    public void update(float delta) {
        frame += delta;
        calc(frame);
    }

    public void calc(float frame) {
        if (nodes == null) return;
        if (nodes.size == 0) return;
        if (rootIndex >= nodes.size) return;
        nodes.get(rootIndex).calc(frame);
    }

    public void clear() {
        for (var node : nodes) {
            node.free();
        }
        nodes.clear();
        rootIndex = 0;
    }

    public void copy(NodeGraph origin) {
        clear();
        for (var node : origin.nodes) {
            add(node.obtainCopy());
        }
        rootIndex = origin.rootIndex;
    }
}
