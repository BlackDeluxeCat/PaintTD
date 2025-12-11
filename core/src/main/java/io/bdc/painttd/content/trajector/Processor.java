package io.bdc.painttd.content.trajector;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.var.*;

public abstract class Processor{
    /** 初始化节点的数组大小, 合理的值有助于节约内存 */
    public int parameterSize;

    /** shift是一个后端为独立的node.shift的output端口, 方便与其他节点建立link. 在updateOutput中推荐直接修改后端. */
    public PortVector2 shift;

    public Array<Linkable> inputs = new Array<>();
    public Array<Linkable> outputs = new Array<>();

    public Processor(){
        this(0);
    }

    public Processor(int parameterSize){
        this.parameterSize = parameterSize;
        registerVars();
    }

    /** 注册变量位. 默认定义shift变量并暴露到outputs. */
    public void registerVars(){
        outputs.add(shift = new PortVector2("shift"){
            @Override
            public void set(Vector2 value, Node node){
                node.shift.set(value);
            }

            @Override
            public Vector2 get(Node node){
                return node.shift;
            }
        });
    }

    /** 为节点设置处理器, 准备合适的内存空间. 适合为节点参数提供初始值. */
    public void initial(Node node){
        node.param.setSize(parameterSize);
        node.links.setSize(inputs.size);
        restart(node);
    }

    /** 将节点状态重置, 默认空实现. 适合为节点状态提供初始值 */
    public void restart(Node node){
    }

    /** 更新节点. 使用inputs的变量, 计算更新outputs的变量. */
    public abstract void update(float frame, Node node);

    /** 更新单个input链接. */
    public void updateInputLink(int inputIndex, float frame, Node node){
        //if(inputIndex < 0 || inputIndex >= inputs.size) return;
        Linkable inputPort = inputs.get(inputIndex);
        if(inputPort == null) return;

        //获取链接
        Link link = node.links.get(inputIndex);
        if(link == null) return;

        //更新上游节点
        Node source = node.tree.nodes.get(link.source);
        if(source.cachedFrame != frame) source.update(frame);

        //同步上游节点数据
        inputPort.readLink(link, node);
    }

    public void updateInputLink(Linkable inputPort, float frame, Node node){
        int index = inputs.indexOf(inputPort, true);
        if(index < 0) return;
        updateInputLink(index, frame, node);
    }

    /** 更新全部inputs链接. */
    public void updateInputLinks(float frame, Node node){
        for(int i = 0; i < inputs.size; i++){
            Linkable inputPort = inputs.get(i);

            Link link = node.links.get(i);
            if(link == null) continue;

            Node source = node.tree.nodes.get(link.source);
            if(source.cachedFrame != frame) source.update(frame);

            inputPort.readLink(link, node);
        }
    }

    /** 创建副本节点的回调 */
    public void copyTo(Node node, Node copy){
    }

    public void build(Node node, Table table){
    }
}
