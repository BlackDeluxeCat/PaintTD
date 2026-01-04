package io.bdc.painttd.game.path;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.var.*;

public class NodeGraph {
    public int rootIndex;
    public Array<Node> nodes = new Array<>();

    public Contexts contexts;

    protected static Array<Node> tmps = new Array<>();
    protected static ObjectIntMap<Node> tmpMap = new ObjectIntMap<>();

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

    public void remapNode(IntArray newIndexes) {
        if (newIndexes.size != nodes.size) {
            throw new IllegalArgumentException("newIndexes size not equal to nodes size");
        }

        tmps.clear();
        for (int i = 0; i < newIndexes.size; i++) {
            Node node = nodes.get(i);
            for (var input : node.inputs) {
                if (input.sourceNode != -1) {
                    input.sourceNode = newIndexes.get(input.sourceNode);
                }
            }
            tmps.set(newIndexes.get(i), node);
        }

        nodes.clear();
        nodes.addAll(tmps);
    }

    /** 创建outputter到inputter的临时链接, 检查是否存在循环依赖 */
    public boolean topoCycleCheck(Node outputter, int outputPort, Node inputter, int inputPort) {
        if (outputter == null || inputter == null) {
            return false;
        }

        // 自链接暂时算循环
        if (outputter == inputter) {
            return true;
        }

        if (outputPort < 0 || outputPort >= outputter.outputs.size) {
            return false;
        }
        if (inputPort < 0 || inputPort >= inputter.inputs.size) {
            return false;
        }

        // 获取对应的端口变量
        LinkableVar outputVar = outputter.outputs.get(outputPort);
        LinkableVar inputVar = inputter.inputs.get(inputPort);

        int originalSourceNode = inputVar.sourceNode;
        int originalSourcePort = inputVar.sourceOutputPort;

        // 临时创建链接
        inputVar.sourceNode = get(outputter);
        inputVar.sourceOutputPort = outputPort;

        boolean hasCycle = topoCycleCheck();

        inputVar.sourceNode = originalSourceNode;
        inputVar.sourceOutputPort = originalSourcePort;

        return hasCycle;
    }

    /** 检查是否存在循环链接 */
    public boolean topoCycleCheck() {
        if (nodes.size == 0) return false;

        tmpMap.clear();

        // 初始化所有节点的入度为0
        for (Node node : nodes) {
            tmpMap.put(node, 0);
        }

        // 如果节点A的输入链接到节点B的输出, 则B的入度+1
        // 谁被对方拿sourceNode指着, 谁即生产者, 有向边的箭头就指向谁, 入度就++
        // 有向边表示计算流的扩散方向. A有输入链接到B的输出, 则消费者A将请求生产者B计算, 应做之事是避免循环请求.
        for (Node node : nodes) {
            for (LinkableVar input : node.inputs) {
                if (input.sourceNode != -1) {
                    Node sourceNode = get(input.sourceNode);
                    if (sourceNode != null) {
                        // sourceNode被node依赖, 所以sourceNode的入度+1
                        tmpMap.getAndIncrement(sourceNode, 0, 1);
                    }
                }
            }
        }

        // 找到所有入度为0的节点(不被任何节点依赖的节点)
        Array<Node> zeroInDegreeNodes = tmps;
        for (ObjectIntMap.Entry<Node> entry : tmpMap) {
            if (entry.value == 0) {
                zeroInDegreeNodes.add(entry.key);
            }
        }

        // Kahn算法: 不断移除入度为0的节点
        int processedCount = 0;
        while (zeroInDegreeNodes.size > 0) {
            Node current = zeroInDegreeNodes.pop();
            processedCount++;

            // 减少依赖当前节点的节点的入度
            for (LinkableVar input : current.inputs) {
                if (input.sourceNode != -1) {
                    Node sourceNode = get(input.sourceNode);
                    if (sourceNode != null) {
                        float newValue = tmpMap.get(sourceNode, 1) - 1;
                        if (newValue == 0) {
                            zeroInDegreeNodes.add(sourceNode);
                        }
                    }
                }
            }
        }

        // 如果处理了所有节点, 说明无循环；否则有循环
        return processedCount != nodes.size;
    }

    public void inject(Contexts runtime) {
        contexts = runtime;
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
