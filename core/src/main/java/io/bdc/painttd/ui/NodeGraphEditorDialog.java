package io.bdc.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.node.*;
import io.bdc.painttd.game.path.var.*;

public class NodeGraphEditorDialog extends BaseDialog {
    NodeGraph graph;

    NodeGraphGroup group;

    public NodeGraphEditorDialog() {
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
        // 使用新metadata系统
        NodeMetaRegistry metaRegistry = NodeMetaRegistry.getInstance();

        // Vector2ScaleNode按钮
        NodeMeta scaleMeta = metaRegistry.getMeta(Vector2ScaleNode.class);
        buttons.add(ActorUtils.wrapper.set(new TextButton(
            scaleMeta.getDisplayName(),
            Styles.sTextB
        )).click(b -> {
            if (graph != null) {
                graph.add(new Vector2ScaleNode());
                rebuild();
            }
        }).actor).growY();

        // TimeOffsetNode按钮
        NodeMeta timeOffsetMeta = metaRegistry.getMeta(TimeOffsetNode.class);
        buttons.add(ActorUtils.wrapper.set(new TextButton(
            timeOffsetMeta.getDisplayName(),
            Styles.sTextB
        )).click(b -> {
            if (graph != null) {
                graph.add(new TimeOffsetNode());
                rebuild();
            }
        }).actor).growY();
    }

    public void show(NodeGraph graph) {
        this.graph = graph;
        rebuild();
        invalidate();
        show();
        pack();
        group.setTranslate(group.getWidth() / 2f, group.getHeight() / 2f);
    }

    public void rebuild() {
        cont.clear();
        cont.add(group).grow();
        group.clear();
        if (graph != null) {
            for (var node : graph.nodes) {
                var t = new NodeTable(node);
                group.addActor(t);
            }
        }
    }

    public static class NodeGraphGroup extends WidgetGroup {
        public Vector2 translate = new Vector2();

        public NodeGraphGroup() {
        }

        public void setTranslate(float x, float y) {
            translate.set(x, y);
        }

        public void translateBy(float x, float y) {
            translate.add(x, y);
        }

        @Override
        protected Matrix4 computeTransform() {
            return super.computeTransform().translate(translate.x, translate.y, 0);
        }

        @Override
        public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
            return super.parentToLocalCoordinates(parentCoords).sub(translate);
        }

        @Override
        public Vector2 localToParentCoordinates(Vector2 localCoords) {
            return super.localToParentCoordinates(localCoords).add(translate);
        }
    }

    public static class NodeTable extends Table {
        protected Node node;
        public Table title, cont;
        public Table inputsTable, outputsTable;

        public NodeTable(Node node) {
            this.node = node;
            setBackground(Styles.white);

            // 使用新metadata系统获取背景色
            NodeMeta meta = node.getMeta();
            Color bgColor = meta.backgroundColor;
            // 设置背景色（如果有定义）
            if (bgColor != null) setColor(bgColor);

            title = new Table();
            cont = new Table();
            cont.setBackground(Styles.black3);
            inputsTable = new Table();
            outputsTable = new Table();

            cont.clear();
            cont.add(inputsTable).growX().top();
            cont.add(outputsTable).width(Styles.buttonSize).growY().top();
            rebuildTitle();
            rebuildInputs();

            defaults().minHeight(Styles.buttonSize);
            add(title).growX();
            row();
            add(cont).growX();

            reLocate();
        }

        public void rebuildInputs() {
            inputsTable.clear();
            inputsTable.defaults().growX().minHeight(Styles.buttonSize).left();

            // 使用新metadata系统
            NodeMeta meta = node.getMeta();
            Array<PortMeta> inputPortMetas = meta.inputPorts;

            for (int i = 0; i < node.inputs.size; i++) {
                Table portRow = new Table();
                PortMeta portMeta = inputPortMetas.get(i);
                LinkableVar var = node.inputs.get(i);

                // 左侧：端口标识
                portRow.add(ActorUtils.wrapper.set(new Label(portMeta.isInput() ? "I" : "O", Styles.sLabel))
                                              .with(l -> ((Label)l).setAlignment(Align.center)).actor
                ).size(Styles.buttonSize);

                // 中间：端口名称
                portRow.add(ActorUtils.wrapper.set(new Label(portMeta.getDisplayName(), Styles.sLabel))
                                              .with(l -> ((Label)l).setAlignment(Align.center)).actor
                ).minHeight(Styles.buttonSize).growX();

                // 右侧：值编辑器（如果有uiBuilder）
                Table editorContainer = new Table();
                boolean hasEditor = portMeta.build(var, editorContainer);
                if (hasEditor) {
                    portRow.add(editorContainer).width(100);
                }

                inputsTable.add(portRow).growX().row();
            }
        }

        public void reLocate() {
            //设置节点位置
            setPosition(node.x, node.y);
            pack();
        }

        public void rebuildTitle() {
            title.clear();

            // 使用新metadata系统获取显示名
            String displayName = node.getMeta().getDisplayName();

            title.add(ActorUtils.wrapper.set(new Label(displayName, Styles.sLabel)).with(l -> {
                Label ll = (Label)l;
                ll.setAlignment(Align.center);
            }).actor).minSize(200f, Styles.buttonSize);
            title.addListener(new DragListener() {
                {
                    setTapSquareSize(10f);
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    super.dragStop(event, x, y, pointer);
                    float dx = x - this.getTouchDownX();
                    float dy = y - this.getTouchDownY();
                    NodeTable.this.moveBy(dx, dy);
                    NodeTable.this.node.x += dx;
                    NodeTable.this.node.y += dy;
                }
            });
        }
    }
}