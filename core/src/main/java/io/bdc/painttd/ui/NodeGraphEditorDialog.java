package io.bdc.painttd.ui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.node.*;

public class NodeGraphEditorDialog extends BaseDialog{
    NodeGraph graph;

    NodeGraphGroup group = new NodeGraphGroup();

    public NodeGraphEditorDialog(){
        super("节点编辑器");
        setBackground(Styles.black8);
        addCloseButton();

        buttons.add(ActorUtils.wrapper.set(new TextButton("缩放节点", Styles.sTextB)).click(b -> {
            if(graph != null){
                graph.add(new ScaleNode());
                rebuild();
            }
        }).actor);

        buttons.add(ActorUtils.wrapper.set(new TextButton("帧偏移节点", Styles.sTextB)).click(b -> {
            if(graph != null){
                graph.add(new TimeOffsetNode());
                rebuild();
            }
        }).actor);
    }

    public void show(NodeGraph graph){
        this.graph = graph;
        rebuild();
        invalidate();
        show();
        layout();
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
            setBackground(Styles.black5);
            pad(10f);
            add(ActorUtils.wrapper.set(new Label(node.getClass().getSimpleName(), Styles.sLabel)).actor).minSize(200f, Styles.buttonSize).growX();
            addListener(new DragListener(){
                {
                    setTapSquareSize(10f);
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer){
                    super.dragStop(event, x, y, pointer);
                    NodeTable.this.moveBy(x - this.getTouchDownX(), y - this.getTouchDownY());
                }
            });
        }
    }

    public static class NodeGraphGroup extends WidgetGroup{
        public Vector2 translate = new Vector2();
        
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
