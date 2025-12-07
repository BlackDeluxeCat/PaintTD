package io.bdc.painttd.content;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.components.logic.*;
import io.bdc.painttd.content.components.logic.physics.*;
import io.bdc.painttd.content.components.logic.target.*;
import io.bdc.painttd.content.components.marker.*;
import io.bdc.painttd.content.components.render.*;

import static io.bdc.painttd.game.Game.*;

public class EntityTypes{
    public static Array<EntityType> types = new Array<>();
    public static ObjectMap<String, Array<EntityType>> typeByCategory = new ObjectMap<>();
    static Array<EntityType> tmp = new Array<>();

    public static void register(EntityType type){
        types.add(type);

        var arr = typeByCategory.get(type.category);
        if(arr == null) typeByCategory.put(type.category, arr = new Array<>());
        arr.add(type);
    }

    public static EntityType getById(String id){
        for(EntityType type : types){
            if(type.id.equals(id)){
                return type;
            }
        }
        return null;
    }

    public static Array<EntityType> getByCategory(String category){
        tmp.clear();
        tmp.addAll(typeByCategory.get(category, tmp));
        return tmp;
    }

    public static EntityType
        //enemies
        eraser,

        //turrets
        building, pencil, brush, airbrush,

        //bullets
        bullet,

        //tiles
        tile, tileStain, spawner;

    public static void create(World world){
        types.clear();

        eraser = new EntityType("eraser", cUnit){
            {
                add(new MarkerComp.UseQuadTree());
                add(new MarkerComp.Hoverable());
                add(new MarkerComp.HitboxScaleByHealth());
                add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.UNIT | CollideComp.BUILDING | CollideComp.FLOOR | CollideComp.OVERLAY));
                add(new TeamComp(1));

                add(new HealthComp(16));
                add(new PositionComp());
                add(new HitboxComp(0.6f).z(1f));

                add(new MoveSpeedComp(1f / lfps));
                add(new VelocityComp());
                add(new MovementNextPathComp());

                add(new PartTextureComp("u-eraser"));
            }
        };

        bullet = new EntityType("bullet", cHide){
            {
                add(new TeamComp(0));
                add(new MarkerComp.UseQuadTree());
                add(new PositionComp());
                add(new HitboxComp(0.2f).z(1));
            }
        };

        building = new EntityType("building", cHide){
            {
                addGroup("building");
                add(new MarkerComp.PlaceSnapGrid());
                add(new MarkerComp.Hoverable());
                add(new CollideComp(CollideComp.BUILDING, false));
                add(new TeamComp(1));
                add(new PositionComp());
                add(new HitboxComp(1).z(1));
            }
        };

        pencil = new EntityType("pencil", building, cBuilding){
            {
                add(new MarkerComp.UseQuadTree());
                add(new TeamComp(0));

                add(new HealthComp(1));

                add(new RangeComp(6));

                add(new MarkerComp.ShootAttacker());
                add(new DamageComp(1));

                add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                add(new TargetPosComp());
                add(new TargetSingleComp());
                add(new CooldownComp(60f));
                add(new ColorLevelComp(2));

                add(new BulletTypeComp(1, new EntityType("pencileBullet", bullet, cHide){
                    {
                        add(new HealthComp(1f));
                        add(new PositionComp());
                        add(new HitboxComp(0.2f).z(1));
                        add(new VelocityComp());
                        add(new MoveSpeedComp(12f / lfps));
                        add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.UNIT));

                        add(new MarkerComp.CollideAttacker());
                        add(new TargetPosComp());
                        add(new TargetSingleComp());
                        add(new StainSplashComp(1));
                    }
                }));

                add(new PartTextureComp("b-pencil"));
            }
        };

        brush = new EntityType("brush", building, cBuilding){
            {
                add(new MarkerComp.UseQuadTree());
                add(new TeamComp(0));

                add(new HealthComp(1));

                add(new RangeComp(10));
                add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                add(new TargetPosComp());
                add(new TargetSingleComp());
                add(new BulletTypeComp(1, new EntityType("brushBullet", bullet, cHide){
                    {
                        add(new HealthComp(1f));
                        add(new PositionComp());
                        add(new HitboxComp(0.2f).z(1));
                        add(new VelocityComp());
                        add(new MoveSpeedComp(12f / lfps));

                        add(new MarkerComp.BulletHoming());
                        add(new MarkerComp.CollideAttacker());
                        add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.UNIT));

                        add(new TargetSingleComp());
                        add(new DamageSplashComp(1, 1));
                        add(new StainSplashComp(1));
                    }
                }));
                add(new CooldownComp(150f));
                add(new ColorLevelComp(1));

                add(new PartTextureComp("b-brush"));
            }
        };

        airbrush = new EntityType("airbrush", building, cBuilding){
            {
                add(new MarkerComp.UseQuadTree());
                add(new TeamComp(0));

                add(new HealthComp(4));

                add(new RangeComp(18));
                add(new BulletTypeComp(4, new EntityType("airbrushBullet", bullet, cHide){
                    {
                        add(new HealthComp(1f));
                        add(new PositionComp().z(1f));
                        add(new VelocityComp());
                        add(new AccelerationComp().z(-1f / lfps));
                        add(new HitboxComp(0.4f).z(0.5f));
                        add(new MoveSpeedComp(4f / lfps));

                        add(new MarkerComp.CollideAttacker());
                        add(new MarkerComp.BulletProjected());
                        add(new MarkerComp.OnCollideDead());
                        add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.OVERLAY));

                        add(new TargetPosComp());
                        add(new DamageSplashComp(1, 2));
                        add(new StainSplashComp(2));
                        //add(new PartTextureComp("bullet-airbrush"));
                    }
                }));
                add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                add(new TargetPosComp());
                add(new TargetSingleComp());
                add(new CooldownComp(200f));
                add(new ColorLevelComp(1));

                add(new PartTextureComp("b-airbrush"));
            }
        };

        tile = new EntityType("tile", cHide){
            {
                addGroup("tile");
                add(new PositionComp());
                add(new HitboxComp(1));
                add(new CollideComp(CollideComp.FLOOR, true));
                add(new TileComp());
                add(new MarkerComp.UseQuadTree());
            }
        };

        tileStain = new EntityType("tileStain", cHide){
            {
                addGroup("tileStain");
                add(new TileStainComp());
                add(new PositionComp());
                add(new HitboxComp(1));
                add(new CollideComp(CollideComp.OVERLAY, true));

                add(new TeamComp(0));
                add(new HealthComp(-1));
                add(new MarkerComp.UseQuadTree());
            }
        };


        spawner = new EntityType("spawner", building, cEditor){
            {
                addGroup("spawner");
                add(new SpawnGroupComp());
                add(new SpawnGroupCompsComp());
            }
        };
    }

    public static String cBuilding = "building", cUnit = "unit", cHide = "hide", cEditor = "editor";
}
