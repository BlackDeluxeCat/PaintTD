package io.bdc.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.Game;
import io.bdc.painttd.io.*;
import io.bdc.painttd.ui.fragment.*;
import io.bdc.painttd.utils.*;
import io.bdc.painttd.utils.func.*;

import static io.bdc.painttd.Core.*;
import static io.bdc.painttd.game.Game.*;
import static io.bdc.painttd.ui.Styles.*;

public class HudGroup extends WidgetGroup {
    //placement brush
    public MapEditBrush current;

    public PlacementFragment placement;

    public HoveredTable hoveredTable;

    public ColorFragment colors;
    public MapEditorFragment mapEditorTable;

    public HudGroup() {
        placement = new PlacementFragment();
        colors = new ColorFragment();
        mapEditorTable = new MapEditorFragment();
        hoveredTable = new HoveredTable();
    }

    public void rebuild() {
        clear();
        setFillParent(true);
        ActorUtils.setVisible(this, g -> Vars.inGame);
        stage.addActor(this);

        fill(t -> {
            t.setName("info");
            t.right().top();
            t.defaults().right();
            t.add(ActorUtils.wrapper.set(new Label("", sLabel))
                                    .update(a -> {
                                        var l = (Label)a;
                                        l.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                                    })
                      .actor);

            t.row();

            t.add(ActorUtils.wrapper.set(new Label("", sLabel))
                                    .update(a -> {
                                        var l = (Label)a;
                                        l.setText("Wave " + rules.wave + "(" + Format.fixedBuilder(rules.waveTimer / lfps, 0) + ")");
                                    })
                      .actor);
        });

        //Placement
        fill(t -> {
            t.setName("placement");
            t.left();
            placement.rebuild();
            t.add(placement).growY();
        });

        fill(t -> {
            t.setName("hovered");
            t.padBottom(80f);
            t.bottom();

            t.add(hoveredTable);
        });

        //Color, Menu
        fill(t -> {
            t.setName("color and menu");
            t.bottom();
            t.defaults().bottom();
            colors.rebuild();
            t.add(colors).growX().bottom();

            mapEditorTable.rebuild();
            t.add(mapEditorTable);

            Table menu = new Table();
            menu.setName("menu");
            t.add(menu).fill();
            menu.defaults().growY().width(3 * buttonSize);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton(i18n.get("button.cancel"), sTextB))
                         .click(b -> {
                             current = null;
                         }).update(b -> b.setVisible(current != null)).actor);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton("", sTextB))
                         .update(b -> ((TextButton)b).getLabel().setText(rules.isPause ? "||" : "▶"))
                         .click(b -> rules.isPause = !rules.isPause)
                         .actor);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton(i18n.get("button.save"), sTextB))
                         .click(b -> {
                             SaveHandler.save("save0");
                         }).actor);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton(i18n.get("button.exit"), sTextB))
                         .click(b -> {
                             Vars.inGame = false;
                             Vars.inMenu = true;
                             Game.endMap();
                         }).actor);
        }).bottom().left();
    }

    public Table fill(Cons<Table> cons) {
        Table table = new Table();
        table.setFillParent(true);
        cons.get(table);
        this.addActor(table);
        return table;
    }

    public abstract static class MapEditBrush {
        public String name;
        public boolean longPress;

        public MapEditBrush(String name) {
            this.name = name;
        }

        public abstract void draw(float worldX, float worldY);
    }

    public abstract static class EntityBrush extends MapEditBrush {
        public EntityType type;

        public EntityBrush(String name) {
            super(name);
        }

        public abstract void getType();

        @Override
        public void draw(float worldX, float worldY) {
            getType();
            var e = type.create();
            if (world.getMapper(MarkerComp.PlaceSnapGrid.class).has(e)) {
                worldX = Math.round(worldX);
                worldY = Math.round(worldY);
            }
            Game.utils.setPosition(e.getId(), worldX, worldY);
        }
    }
}
