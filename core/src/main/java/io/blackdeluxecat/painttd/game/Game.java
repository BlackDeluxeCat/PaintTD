package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.managers.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.game.pathfind.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.map.*;
import io.blackdeluxecat.painttd.struct.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.systems.render.*;
import io.blackdeluxecat.painttd.systems.request.*;
import io.blackdeluxecat.painttd.systems.utils.*;

import java.awt.*;

public class Game{
    public static float lfps = 60;
    public static World world;
    public static Map map = new Map();
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();
    public static GroupManager groups = new GroupManager();

    public static FlowField flowField;

    /**每帧开始时重建树, 需自行检查在当前帧中失效的单位.*/
    public static QuadTree entities = new QuadTree();

    public static CollideQueue collideQueue = new CollideQueue();
    public static DamageQueue damageQueue = new DamageQueue();

    public static StaticUtils utils = new StaticUtils();

    public static void create(){
        createSystems();
        loadMap();
    }

    /**重建整个世界, 加载地图.*/
    public static void loadMap(){
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.register(lm);

        //systems
        lm.lm.layers.forEach(layer -> layer.objects.forEach(builder::with));
        builder.with(groups);

        if(world != null) world.dispose();
        world = new World(builder.build());

        //为单位创建默认组件。def中的组件来自new构造，没有进入池化管理，copy到world中的过程也只是属性拷贝，不涉及池化管理。
        Entities.create();

        ColorPalette palette = new ColorPalette();
        palette.addColor(Color.FOREST.toIntBits());
        palette.addColor(Color.RED.toIntBits());
        palette.addColor(Color.ROYAL.toIntBits());
        palette.addColor(Color.YELLOW.toIntBits());
        palette.addColor(Color.PURPLE.toIntBits());

        map.create(world, 30, 20, palette);
        Vars.hud.mapEditorTable.buildColorPalette();
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                map.putEntity(Entities.tileStain.create().getId(), "tileStain", x, y);
            }
        }
        flowField = new FlowField(map);
        flowField.rebuild();
    }

    /**循环系统层级, z越小, 越早执行*/
    public static LayerManager.Layer<BaseSystem>
        logicPre = lm.lm.registerLayer("logicFirstWork", 0),
        logicCollide = lm.lm.registerLayer("logicCollide", 100),
        logicAI = lm.lm.registerLayer("logicAI", 200),
        logic = lm.lm.registerLayer("logic", 300),
        logicPost = lm.lm.registerLayer("logicLastWork", 9000),
        render = lm.lm.registerLayer("render", 10000);

    public static void createSystems(){
        logicPre.with(l -> {
            l.add(utils);
            l.add(new FlowFieldCoreChangeDetect());
            l.add(new FlowFieldUpdate());
            l.add(new RebuildQuadTree());
        });

        logicCollide.with(l -> {
            l.add(new CollideQueueRemoveNoLongerOverlaps());
            l.add(new CollideDetect());
            l.add(new CollideEnemyRequestDamage());
            l.add(new TileStainRequestDamage());
        });

        logicAI.with(l -> {
            l.add(new TargetFind());
            l.add(new CooldownShoot());
            l.add(new MovementVelGenFlowField());
            l.add(new MovementVelPush());
        });

        logic.with(l -> {
            l.add(new EnergyRegenerate());

            l.add(new DamageTypeCollideApply());
            l.add(new DamageTypeDirectApply());
        });

        logicPost.with(l -> {
            l.add(new PostDamageQueue());

            l.add(new MarkHealthDead());
            l.add(new RemoveDead());
        });

        render.with(l -> {
            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                    Core.batch.begin();
                }
            });

            l.add(new DrawPartTexture());

            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    Core.batch.end();
                }
            });

            //使用了ShapeRenderer的系统
            l.add(new DrawMapGrid());
            l.add(new DrawColoredTileStain());
            l.add(new DrawUnitHitbox());
            l.add(new DrawTarget());
        });
    }

}
