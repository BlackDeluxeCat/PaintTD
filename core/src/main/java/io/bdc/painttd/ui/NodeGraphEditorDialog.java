package io.bdc.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.game.path.metadata.*;
import io.bdc.painttd.game.path.node.*;
import io.bdc.painttd.game.path.var.*;
import io.bdc.painttd.io.*;
import io.bdc.painttd.render.*;
import io.bdc.painttd.utils.func.*;

public class NodeGraphEditorDialog extends BaseDialog {
    protected FileHandle file = Core.gameDataFolder.child("nodeGraphExport.json");
    public NodeGraphGroup group;

    public NodeGraphEditorDialog() {
        super("节点编辑器");
        setBackground(Styles.black8);
        group = new NodeGraphGroup();

        addCloseButton();
        // 使用metadata创建节点按钮
        createNodeButtons();
        rebuild();
    }

    /**
     * 使用metadata系统创建节点按钮
     */
    protected void createNodeButtons() {
        //序列化导出
        buttons.add(ActorUtils.wrapper
                        .set(new TextButton("导出", Styles.sTextB))
                        .click(b -> {
                            if (group.graph != null) {
                                //Gdx.app.getClipboard().setContents(JsonIO.json.toJson(group.graph, NodeGraph.class));
                                file.writeString(JsonIO.json.prettyPrint(JsonIO.json.toJson(group.graph, NodeGraph.class)), false);
                            }
                        }).actor)
               .growY();

        Cons2<Class<? extends Node>, Prov<? extends Node>> addButton = (clazz, prov) -> {
            NodeMeta meta = NodeMetaRegistry.getInstance().getMeta(clazz);
            buttons.add(ActorUtils.wrapper.set(new TextButton(
                meta.getDisplayName(),
                Styles.sTextB
            )).click(b -> {
                if (group.graph != null) {
                    group.graph.add(prov.get());
                    group.rebuild();
                }
            }).actor).growY();
        };

        addButton.get(EntitySetVelocityNode.class, EntitySetVelocityNode::new);
        addButton.get(EntityPositionNode.class, EntityPositionNode::new);
        addButton.get(EntityTargetPositionNode.class, EntityTargetPositionNode::new);
        addButton.get(Vector2AddNode.class, Vector2AddNode::new);
        addButton.get(Vector2ScaleNode.class, Vector2ScaleNode::new);
        addButton.get(TimeOffsetNode.class, TimeOffsetNode::new);
    }

    public void show(NodeGraph graph) {
        group.graph = graph;
        group.rebuild();
        group.setTranslate(group.getWidth() / 2f, group.getHeight() / 2f);
        invalidate();
        show();
        pack();
    }

    public void rebuild() {
        cont.clear();
        group.rebuild();
        cont.add(group).grow();
    }

    public static class NodeGraphGroup extends WidgetGroup {
        public NodeGraph graph;

        public Array<NodeElem> nodeElems = new Array<>();

        public Vector2 translate = new Vector2();

        public NodeGraphGroup() {
        }

        public void setNodeGraph(NodeGraph graph) {
            this.graph = graph;
            rebuild();
        }

        public void rebuild() {
            clear();
            nodeElems.clear();
            if (graph != null) {
                //重建节点元素
                for (int i = 0; i < graph.nodes.size; i++) {
                    var node = graph.nodes.get(i);
                    var t = new NodeElem(node);
                    nodeElems.add(t);
                    addActor(t);
                }

                //重连端口
                for (int i = 0; i < nodeElems.size; i++) {
                    NodeElem current = nodeElems.get(i);
                    for (var currentPort : current.inputElems) {
                        LinkableVar currentInputVar = currentPort.linkableVar;
                        //以下寻找源节点的output端口元素
                        if(currentInputVar.sourceNode != -1){
                            Node sourceNode = graph.get(currentInputVar.sourceNode);
                            NodeElem sourceNodeElem = findNodeElem(sourceNode);
                            if(sourceNodeElem != null){
                                LinkableVar sourceOutputVar = sourceNode.getOutput(currentInputVar.sourceOutputPort);
                                PortElem sourcePort = sourceNodeElem.findOutputPortElem(sourceOutputVar);
                                if(sourcePort != null){
                                    //已经拿到源节点output端口了, 和当前节点的input端口链接
                                    currentPort.linkPort = sourcePort;
                                }
                            }
                        }
                    }
                }
            }
        }

        public NodeElem findNodeElem(Node node) {
            for (var elem : nodeElems) {
                if (elem.node == node) return elem;
            }
            return null;
        }

        public class NodeElem extends Table {
            protected Node node;
            protected NodeMeta meta;

            public Table title, cont;
            public Array<PortElem> inputElems = new Array<>(), outputElems = new Array<>();
            public Table inputPortTable, outputPortTable;

            public NodeElem(Node node) {
                this.node = node;
                setBackground(Styles.white);

                meta = node.getMeta();
                Color bgColor = meta.backgroundColor;
                // 设置背景色（如果有定义）
                if (bgColor != null) setColor(bgColor);

                title = new Table();
                cont = new Table();
                inputPortTable = new Table();
                outputPortTable = new Table();

                rebuildTitle();
                rebuildInputs();
                rebuildOutputs();

                defaults().minSize(Styles.buttonSize);
                add(title).growX();
                row();
                add(cont).grow().top();

                cont.setBackground(Styles.black3);
                cont.defaults().growX().top().minSize(Styles.buttonSize);
                cont.add(outputPortTable);
                cont.row();
                cont.add(inputPortTable);

                reLocate();
            }

            public void reLocate() {
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
                        NodeElem.this.moveBy(dx, dy);
                        NodeElem.this.node.x += dx;
                        NodeElem.this.node.y += dy;
                    }
                });
            }

            public void rebuildInputs() {
                inputElems.clear();
                inputPortTable.clear();
                inputPortTable.defaults().growX().minHeight(Styles.buttonSize).left();

                for (int i = 0; i < node.inputs.size; i++) {
                    Table portRow = new Table();
                    PortMeta portMeta = node.getInputMeta(i);
                    LinkableVar var = node.getInput(i);

                    // 左侧: 端口标识
                    PortElem port = new PortElem(this, i, true);
                    inputElems.add(port);
                    portRow.add(port).size(Styles.buttonSize);

                    // 右侧: 值编辑器
                    Table editorContainer = new Table();
                    boolean hasEditor = portMeta.build(var, editorContainer);
                    if (hasEditor) {
                        portRow.add(editorContainer).minWidth(100).growX();
                    } else {
                        portRow.add(new Label(portMeta.getDisplayName(), Styles.sLabel)).minWidth(100).growX();
                    }

                    inputPortTable.add(portRow).growX().row();
                }
            }

            public void rebuildOutputs() {
                outputElems.clear();
                outputPortTable.clear();
                outputPortTable.defaults().minHeight(Styles.buttonSize).right();

                for (int i = 0; i < node.outputs.size; i++) {
                    Table portRow = new Table();
                    PortMeta portMeta = node.getOutputMeta(i);
                    LinkableVar var = node.getOutput(i);

                    // 左侧: 值编辑器
                    Table editorContainer = new Table();
                    boolean hasEditor = portMeta.build(var, editorContainer);
                    if (hasEditor) {
                        portRow.add(editorContainer).minWidth(100).growX().right();
                    } else {
                        Label l = new Label(portMeta.getDisplayName(), Styles.sLabel);
                        l.setAlignment(Align.right);
                        portRow.add(l).minWidth(100).growX();
                    }

                    // 右侧: 端口标识
                    PortElem port = new PortElem(this, i, false);
                    outputElems.add(port);
                    portRow.add(port).size(Styles.buttonSize);

                    outputPortTable.add(portRow).growX().row();
                }
            }

            public PortElem findInputPortElem(LinkableVar var){
                for (var elem : inputElems) {
                    if (elem.linkableVar == var) return elem;
                }
                return null;
            }

            public PortElem findOutputPortElem(LinkableVar var){
                for (var elem : outputElems) {
                    if (elem.linkableVar == var) return elem;
                }
                return null;
            }
        }

        public class PortElem extends Table {
            public static NodeGraphGroup.PortElem draggingElem, hitElem;
            public static boolean dragging;

            protected NodeElem nodeElem;
            protected LinkableVar linkableVar;
            protected PortMeta meta;
            public int idx;
            public boolean isInput;

            /** 与本端口链接的外部端口, 总是output */
            public PortElem linkPort;

            public PortElem(NodeElem myNode, int myIndex, boolean isInput) {
                this.nodeElem = myNode;
                this.meta = isInput ? myNode.node.getInputMeta(myIndex) : myNode.node.getOutputMeta(myIndex);
                this.idx = myIndex;
                this.isInput = isInput;
                this.linkableVar = isInput ? nodeElem.node.getInput(idx) : nodeElem.node.getOutput(idx);

                setTouchable(Touchable.enabled);

                addListener(new DragListener() {
                    final Vector2 v = new Vector2();

                    {
                        setTapSquareSize(40f);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (super.touchDown(event, x, y, pointer, button)) {
                            draggingElem = NodeGraphGroup.PortElem.this;
                            dragging = true;
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        super.touchDragged(event, x, y, pointer);
                        if (dragging) {
                            NodeGraphGroup.PortElem.this.localToStageCoordinates(v.set(x, y));
                            Actor hit = UI.stage.hit(v.x, v.y, true);
                            if (hit != null) {
                                Actor find = ActorUtils.findDescendantOf(hit, a -> a instanceof NodeGraphGroup.PortElem);
                                if (find != null && find != NodeGraphGroup.PortElem.this) {
                                    hitElem = (NodeGraphGroup.PortElem)find;//can be null
                                } else {
                                    hitElem = null;
                                }
                            }
                        }
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        if (draggingElem == NodeGraphGroup.PortElem.this) {
                            if (hitElem != null) {
                                if (hitElem != draggingElem) {
                                    draggingElem.createLink(hitElem);
                                }
                            } else {
                                draggingElem.createLink(null);
                            }

                            draggingElem = null;
                            dragging = false;
                        }
                    }
                });

                Image img = new Image(Core.atlas.findRegion("ui-node-port"));
                add(img).grow().getActor().setColor(meta.color);
            }

            /** 该方法自动识别IO */
            public void createLink(@Null NodeGraphGroup.PortElem other) {
                if (other == null || other.nodeElem.node == this.nodeElem.node || other.isInput == isInput) {
                    clearLink();
                    return;
                }

                PortElem ine, oute;
                LinkableVar in, out;
                if (isInput) {
                    in = linkableVar;
                    ine = this;
                    out = other.linkableVar;
                    oute = other;
                } else {
                    out = linkableVar;
                    oute = this;
                    in = other.linkableVar;
                    ine = other;
                }

                if (in == null || out == null) return;
                //循环检查
                if (graph.topoCycleCheck(oute.nodeElem.node, oute.idx, ine.nodeElem.node, ine.idx)) {
                    clearLink();
                    return;
                }

                //使用转发端口做类型判断
                LinkableVar forwardingOut = oute.nodeElem.node.getSyncOutput(0, oute.idx);
                if (in.canLink(forwardingOut)) {
                    in.sourceNode = graph.get(oute.nodeElem.node);
                    in.sourceOutputPort = oute.idx;
                    ine.linkPort = oute;
                }
            }

            public void clearLink(){
                linkPort = null;
                linkableVar.sourceNode = -1;
                linkableVar.sourceOutputPort = -1;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);

                if (isTransform()) applyTransform(batch, computeTransform());

                Renderer.setColor(Color.WHITE);
                Renderer.line.setStroke(2f);

                if (linkPort != null) {
                    localToAscendantCoordinates(NodeGraphGroup.this, tmp.set(getWidth(), getHeight()).scl(0.5f));
                    linkPort.localToAscendantCoordinates(NodeGraphGroup.this, tmp2.set(linkPort.getWidth(), linkPort.getHeight()).scl(0.5f));

                    float off = tmp3.set(tmp).sub(tmp2).len() / 2f;
                    drawLink(tmp.x, tmp.y, tmp2.x, tmp2.y, -off, off);
                }

                if (dragging && draggingElem == this) {
                    localToAscendantCoordinates(NodeGraphGroup.this, tmp.set(getWidth(), getHeight()).scl(0.5f));
                    NodeGraphGroup.this.stageToLocalCoordinates(tmp2.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
                    Renderer.a(0.5f);

                    float off = tmp3.set(tmp).sub(tmp2).len() / 2f * (draggingElem.isInput ? -1f : 1f);
                    drawLink(tmp.x, tmp.y, tmp2.x, tmp2.y, off, -off);
                }

                if (isTransform()) resetTransform(batch);
            }
        }

        public static Bezier<Vector2> curve = new Bezier<>();

        public static Vector2 vstart = new Vector2(), vend = new Vector2(), p1 = new Vector2(), p2 = new Vector2(), tmp = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();

        public void drawLink(float inX, float inY, float outX, float outY, float inOffset, float outOffset) {
            vstart.set(inX, inY);
            p1.set(inX + inOffset, inY);
            p2.set(outX + outOffset, outY);
            vend.set(outX, outY);
            curve.set(vstart, p1, p2, vend);
            Renderer.setColor(Color.WHITE);
            Renderer.line.setStroke(4f);

            Renderer.line.polylineStart();
            float segments = 16;
            for (int i = 0; i <= segments; i++) {
                curve.valueAt(tmp, i / segments);
                Renderer.line.polylineAdd(tmp.x, tmp.y);
            }
            Renderer.line.polylineEnd();
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
}