package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.ui.fragment.*;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.groups;
import static io.blackdeluxecat.painttd.game.Game.world;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class Hud{
    public MapEditBrush current;

    public WidgetGroup group;
    public Table buttons = new Table();
    public MapEditorTable mapEditorTable;

    public void create(){
        group = new WidgetGroup();
        group.setFillParent(true);
        stage.addActor(group);
        stage.setDebugAll(true);

        mapEditorTable = new MapEditorTable();

        fill(t -> {
            t.left().top();
            t.defaults().left();
            t.add(new Label("Paint Tower Defence", sLabel)).row();
            t.add(new Label("1", sLabel){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                    super.draw(batch, parentAlpha);
                }
            }).row();
            t.add(new ActorUtils<>(new Label("", sLabel)).update(l -> l.setText("G|Unit: " + groups.getEntities("unit").size())).actor).row();
            t.add(new ActorUtils<>(new Label("", sLabel)).update(l -> l.setText("G|Building: " + groups.getEntities("building").size())).actor);
        });

        //Placement
        fill(t -> {
            buttons.defaults().height(buttonSize * 2).pad(2).minWidth(buttonSize);

            buttons.add(new ActorUtils<>(new Table()).with(line1 -> {
                Label label = new Label("", sLabel);
                ActorUtils.wrapper.set(label).update(l -> ((Label)l).setText("选择建筑: " + (current == null ? "" : current.name)));
                line1.add(label);

                line1.add(new ActorUtils<>(new Table()).with(t1 -> {
                    t1.add(ActorUtils.wrapper
                            .set(new TextButton("敌人", sTextB))
                            .click(b -> {
                                current = bEnemy;
                            })
                            .actor);

                    t1.add(ActorUtils.wrapper
                            .set(new TextButton("基础塔", sTextB))
                            .click(b -> {
                                current = bTower;
                            })
                            .actor);

                }).actor).growX();

            }).actor).growX().row();

            buttons.add(mapEditorTable).growX().row();

            t.add(buttons).growX();

            t.add(new ActorUtils<>(new Table()).with(rt -> {
                rt.add(ActorUtils.wrapper
                          .set(new TextButton("取消", sTextB))
                          .click(b -> {
                              current = null;
                          }).update(b -> b.setVisible(current != null)).actor).width(4 * buttonSize).growY();
            }).actor).fill();

            t.add(new ActorUtils<>(new Table()).with(rt -> {
                rt.add(ActorUtils.wrapper
                           .set(new TextButton("退出", sTextB))
                           .click(b -> {
                               Gdx.app.exit();
                           }).actor).width(4 * buttonSize).growY();
            }).actor).fill();
        }).bottom().left();
    }

    public Table fill(Consumer<Table> cons){
        Table table = new Table();
        table.setFillParent(true);
        cons.accept(table);
        group.addActor(table);
        return table;
    }

    public abstract static class MapEditBrush{
        public String name;

        public MapEditBrush(String name){
            this.name = name;
        }

        public abstract void draw(float worldX, float worldY);
    }

    public abstract static class EntityBrush extends MapEditBrush{
        public EntityType type;

        public EntityBrush(String name){
            super(name);
        }

        public abstract void getType();

        @Override
        public void draw(float worldX, float worldY){
            getType();
            var e = type.create();
            if(world.getMapper(MarkerComp.PlaceSnapGrid.class).has(e)){
                worldX = Math.round(worldX);
                worldY = Math.round(worldY);
            }
            Game.utils.setPosition(e.getId(), worldX, worldY);
        }
    }

    public EntityBrush bEnemy = new EntityBrush("敌人"){
                @Override
                public void getType(){
                    type = Entities.eraser;
                }
            },
            bTower = new EntityBrush("基础塔"){
                @Override
                public void getType(){
                    type = Entities.pencil;
                }
            };
}
