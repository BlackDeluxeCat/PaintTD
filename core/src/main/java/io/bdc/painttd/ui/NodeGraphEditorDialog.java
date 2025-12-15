package io.bdc.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.node.*;

public class NodeGraphEditorDialog extends BaseDialog{
    NodeGraph graph;

    NodeGraphGroup group;

    public NodeGraphEditorDialog(){
        super("节点编辑器");
        setBackground(Styles.black8);
        group = new NodeGraphGroup();

        addCloseButton();
        // 使用metadata创建节点按钮
        createNodeButtons();
    }
    
    /**
     * 使用metadata系统创建节点按钮
     */
    private void createNodeButtons() {
        // ScaleNode按钮
        NodeMetadata scaleMetadata = NodeMetadataRegistry.getInstance().getMetadata(ScaleNode.class);
        buttons.add(ActorUtils.wrapper.set(new TextButton(
            scaleMetadata.getDisplayName(),
            Styles.sTextB
        )).click(b -> {
            if(graph != null){
                graph.add(new ScaleNode());
                rebuild();
            }
        }).actor).growY();

        // TimeOffsetNode按钮
        NodeMetadata timeOffsetMetadata = NodeMetadataRegistry.getInstance().getMetadata(TimeOffsetNode.class);
        buttons.add(ActorUtils.wrapper.set(new TextButton(
            timeOffsetMetadata.getDisplayName(),
            Styles.sTextB
        )).click(b -> {
            if(graph != null){
                graph.add(new TimeOffsetNode());
                rebuild();
            }
        }).actor).growY();
    }

    public void show(NodeGraph graph){
        this.graph = graph;
        rebuild();
        invalidate();
        show();
        pack();
        group.setTranslate(group.getWidth() / 2f, group.getHeight() / 2f);
    }

    public void rebuild(){
        cont.clear();
        cont.add(group).grow();
        group.clear();
        if(graph != null){
            for(var node : graph.nodes){
                var t = new NodeTable(node);
                group.addActor(t);
            }
        }
    }

    public static class NodeTable extends Table{
        protected Node node;
        
        public NodeTable(Node node){
            this.node = node;
            setBackground(Styles.white);
            
            // 使用metadata获取节点显示名称和背景色
            NodeMetadata metadata = NodeMetadataRegistry.getInstance().getMetadata(node.getClass());
            String displayName = metadata.getDisplayName();
            Color bgColor = metadata.backgroundColor;  // 直接访问public字段

            // 设置背景色（如果有定义）
            if(bgColor != null) setColor(bgColor);
            
            add(ActorUtils.wrapper.set(new Label(displayName, Styles.sLabel)).with(l -> {
                Label ll = (Label)l;
                ll.setAlignment(Align.center);
            }).actor).minSize(200f, Styles.buttonSize);
            addListener(new DragListener(){
                {
                    setTapSquareSize(10f);
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer){
                    super.dragStop(event, x, y, pointer);
                    float dx = x - this.getTouchDownX();
                    float dy = y - this.getTouchDownY();
                    NodeTable.this.moveBy(dx, dy);
                    NodeTable.this.node.x += dx;
                    NodeTable.this.node.y += dy;
                }
            });

            row();

            //设置节点配置栏
            add(new Table()).minSize(100f, Styles.buttonSize);

            //设置节点位置
            setPosition(node.x, node.y);
            pack();
        }
    }

    public static class NodeGraphGroup extends WidgetGroup{
        public Vector2 translate = new Vector2();
        
        public NodeGraphGroup(){
        }
        
        public void setTranslate(float x, float y){
            translate.set(x, y);
        }
        
        public void translateBy(float x, float y){
            translate.add(x, y);
        }

        @Override
        protected Matrix4 computeTransform(){
            return super.computeTransform().translate(translate.x, translate.y, 0);
        }

        @Override
        public Vector2 parentToLocalCoordinates(Vector2 parentCoords){
            return super.parentToLocalCoordinates(parentCoords).sub(translate);
        }

        @Override
        public Vector2 localToParentCoordinates(Vector2 localCoords){
            return super.localToParentCoordinates(localCoords).add(translate);
        }
    }
}