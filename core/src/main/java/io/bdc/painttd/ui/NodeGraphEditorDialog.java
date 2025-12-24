package io.bdc.painttd.ui;

import com.badlogic.gdx.*;
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
import io.bdc.painttd.render.*;

public class NodeGraphEditorDialog extends BaseDialog {

    public NodeGraphGroup group;

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
            if (group.graph != null) {
                group.graph.add(new Vector2ScaleNode());
                rebuild();
            }
        }).actor).growY();

        // TimeOffsetNode按钮
        NodeMeta timeOffsetMeta = metaRegistry.getMeta(TimeOffsetNode.class);
        buttons.add(ActorUtils.wrapper.set(new TextButton(
            timeOffsetMeta.getDisplayName(),
            Styles.sTextB
        )).click(b -> {
            if (group.graph != null) {
                group.graph.add(new TimeOffsetNode());
                group.rebuild();
            }
        }).actor).growY();
    }

    public void show(NodeGraph graph) {
        group.graph = graph;
        group.rebuild();
        invalidate();
        show();
        pack();
        group.setTranslate(group.getWidth() / 2f, group.getHeight() / 2f);
    }

    public void rebuild() {
        cont.clear();
        group.rebuild();
        cont.add(group).grow();
    }

    public static class NodeGraphGroup extends WidgetGroup {
        public NodeGraph graph;

        public Vector2 translate = new Vector2();

        public NodeGraphGroup() {
        }

        public void rebuild() {
            clear();
            if (graph != null) {
                for (var node : graph.nodes) {
                    var t = new NodeElem(node);
                    addActor(t);
                }
            }
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

        public class NodeElem extends Table {
            protected Node node;
            public Table title, cont;
            public Table inputsPorts, outputPorts;

            public NodeElem(Node node) {
                this.node = node;
                setBackground(Styles.white);

                // 使用新metadata系统获取背景色
                NodeMeta meta = node.getMeta();
                Color bgColor = meta.backgroundColor;
                // 设置背景色（如果有定义）
                if (bgColor != null) setColor(bgColor);

                title = new Table();
                cont = new Table();
                inputsPorts = new Table();
                outputPorts = new Table();

                defaults().minSize(Styles.buttonSize);
                add(title).growX();
                row();
                add(cont).grow().top();

                cont.setBackground(Styles.black3);
                cont.defaults().growX().top().minSize(Styles.buttonSize);
                cont.add(outputPorts);
                cont.row();
                cont.add(inputsPorts);

                rebuildTitle();
                rebuildInputs();

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
                inputsPorts.clear();
                inputsPorts.defaults().growX().minHeight(Styles.buttonSize).left();

                for (int i = 0; i < node.inputs.size; i++) {
                    Table portRow = new Table();
                    PortMeta portMeta = node.getInputMeta(i);
                    LinkableVar var = node.inputs.get(i);

                    // 左侧：端口标识
                    portRow.add(new PortElem(node, i, true)).size(Styles.buttonSize);

                    // 右侧：值编辑器（如果有uiBuilder）
                    Table editorContainer = new Table();
                    boolean hasEditor = portMeta.build(var, editorContainer);
                    if (hasEditor) {
                        portRow.add(editorContainer).minWidth(100).growX();
                    } else {
                        portRow.add(new Label(portMeta.getDisplayName(), Styles.sLabel)).minWidth(100).growX();
                    }

                    inputsPorts.add(portRow).growX().row();
                }
            }
        }

        public class PortElem extends Table {
            public static NodeGraphGroup.PortElem draggingElem, hitElem;
            public static boolean dragging;

            public Node node;
            public int idx;
            public boolean isInput;

            public PortElem linkPort;

            public Bezier<Vector2> curve = new Bezier<>();
            public static Vector2 vstart = new Vector2(), vend = new Vector2(), p1 = new Vector2(), p2 = new Vector2(), tmp = new Vector2(), tmp2 = new Vector2();

            public PortElem(Node myNode, int myIndex, boolean isInput) {
                this.node = myNode;
                this.idx = myIndex;
                this.isInput = isInput;

                //setTransform(true);
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
                        if(dragging) {
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
                PortMeta meta = isInput ? node.getInputMeta(idx) : node.getOutputMeta(idx);
                add(img).grow().getActor().setColor(meta.color);
            }

            public void createLink(@Null NodeGraphGroup.PortElem other) {
                if (other == null || other.node == this.node || other.isInput != isInput) {
                    linkPort = null;
                    return;
                }

                PortElem ine, oute;
                LinkableVar in, out;
                if (isInput) {
                    in = getVar();
                    ine = this;
                    out = other.getVar();
                    oute = other;
                } else {
                    out = getVar();
                    oute = this;
                    in = other.getVar();
                    ine = other;
                }

                if (in == null || out == null) return;
                if (in.canLink(out)) {
                    in.sourceNode = graph.get(other.node);
                    in.sourceOutputPort = other.idx;
                    ine.linkPort = oute;
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                if (linkPort != null) {
                    tmp.set(getX(Align.center), getY(Align.center));//这个是正常地渲染到节点图标上了
                    tmp2.set(linkPort.getX(Align.center), linkPort.getY(Align.center));//这样只能拿到似乎是NodeElem坐标系的坐标, 直接拿去渲染就会指向左下角
                    linkPort.localToAscendantCoordinates(NodeGraphGroup.this, tmp2);//这样就稍微好一点, 但是随着双亲元素(portRow)高的不同, 产生不同的向上偏移
                    drawLink(tmp.x, tmp.y, tmp2.x, tmp2.y);
                }

                if (draggingElem == this) {
                    tmp.set(getX(Align.center), getY(Align.center));
                    tmp2.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                    NodeGraphGroup.this.stageToLocalCoordinates(tmp2);
                    if(isInput) {
                        drawLink(tmp.x, tmp.y, tmp2.x, tmp2.y);
                    } else {
                        drawLink(tmp2.x, tmp2.y, tmp.x, tmp.y);
                    }
                }
            }

            public void drawLink(float inX, float inY, float outX, float outY) {
                vstart.set(inX, inY);
                p1.set(inX - 10f, inY);
                p2.set(outX + 10f, outY);
                vend.set(outX, outY);
                curve.set(vstart, p1, p2, vend);
                Renderer.setColor(Color.WHITE);
                Renderer.line.setStroke(4f);

                Renderer.line.begin();
                float segments = 10;
                for (int i = 0; i < segments + 1; i++) {
                    curve.valueAt(tmp, i / segments);
                    Renderer.line.point(tmp.x, tmp.y);
                }
                Renderer.line.end();
            }

            public LinkableVar getVar() {
                return isInput ? node.inputs.get(idx) : node.outputs.get(idx);
            }
        }
    }
}