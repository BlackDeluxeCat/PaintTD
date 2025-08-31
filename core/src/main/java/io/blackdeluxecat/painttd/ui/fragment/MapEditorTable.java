package io.blackdeluxecat.painttd.ui.fragment;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.game.Game.world;
import static io.blackdeluxecat.painttd.ui.Styles.sLabel;
import static io.blackdeluxecat.painttd.ui.Styles.sTextB;

public class MapEditorTable extends Table{
    public int selectColorIndex = 0;

    public Table colorTable = new Table();

    public MapEditorTable(){
        create();
    }

    public void create(){
        add(ActorUtils.wrapper.set(new Label("编辑世界", sLabel)).actor);

        add(new ActorUtils<>(new Table()).with(t1 -> {
            t1.add(ActorUtils.wrapper
                       .set(new TextButton("重置世界", sTextB))
                       .click(b -> Game.loadMap())
                       .actor);

            t1.add(ActorUtils.wrapper
                       .set(new TextButton("设置核心", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawStainCore;
                       })
                       .actor);

            t1.add(ActorUtils.wrapper
                       .set(new TextButton("绘制墙体", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawWall;
                       })
                       .actor);

            t1.add(ActorUtils.wrapper
                       .set(new TextButton("染色", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawStainColor;
                       })
                       .actor);

            t1.add(colorTable);
        }).actor).growX();
    }

    public void buildColorPalette(){
        colorTable.clear();
        colorTable.defaults().size(Styles.buttonSize);
        for(int i = 0; i < Game.map.colorPalette.colors.size; i++){
            int finalI = i;
            colorTable.add(new ActorUtils<>(new Button(sTextB)).with(b -> {
                b.add(new Image(Styles.whited)).grow().pad(2f).getActor().setColor(Vars.c1.set(Game.map.colorPalette.getColor(finalI)));
            }).click(b -> selectColorIndex = finalI).actor);
        }
    }

    public Hud.MapEditBrush drawWall = new Hud.MapEditBrush("地形墙"){
        @Override
        public void draw(float wx, float wy){
            Game.map.get(Math.round(wx), Math.round(wy)).setWall(true);
        }
    }, drawStainCore = new Hud.MapEditBrush("核心"){
        @Override
        public void draw(float wx, float wy){
            var e = Game.map.getEntity(Math.round(wx), Math.round(wy), "tileStain");
            if(e == -1) return;
            var mapper = world.getMapper(TileStainComp.class);
            if(!mapper.has(e)) return;
            var stain = mapper.get(e);
            stain.isCore = true;
        }
    }, drawStainColor = new Hud.MapEditBrush("地形染色"){
        @Override
        public void draw(float worldX, float worldY){
            var e = Game.map.getEntity(Math.round(worldX), Math.round(worldY), "tileStain");
            if(e == -1) return;
            var mapper = world.getMapper(HealthComp.class);
            if(!mapper.has(e)) return;
            var hp = mapper.get(e);
            hp.health = selectColorIndex + 1;
        }
    };
}
