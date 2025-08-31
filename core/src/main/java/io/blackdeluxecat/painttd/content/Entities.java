package io.blackdeluxecat.painttd.content;

import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.content.components.render.*;

import static io.blackdeluxecat.painttd.game.Game.lfps;

public class Entities{
    public static EntityType
        //enemies
        unit, eraser, tileStain,
        //turrets
        building, pencil ,brush;

    public static void createBuilding(){
        building = new EntityType("building"){
            {
                groups.add("building");
                def.add(new MarkerComp.PlaceSnapGrid());
                def.add(new PositionComp());
                def.add(new HitboxComp(1));
            }
        };

        pencil = new EntityType("pencil", building){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new TeamComp(0));

                def.add(new HealthComp(1));

                def.add(new EnergyComp(2));
                def.add(new EnergyRegenComp(2 / lfps));

                def.add(new RangeComp(8));
                def.add(new DamageComp(1));
                def.add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                def.add(new TargetComp());
                def.add(new CooldownComp(60f));
                def.add(new ColorLevelComp());

                def.add(new PartTextureComp("b-pencil"));

                def.add(new BuildTypeComp(BuildTypeComp.GROUP_DAMAGE));
            }
        };

        //数值未调整
        brush = new EntityType("brush", building){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new TeamComp(0));

                def.add(new HealthComp(1));

                def.add(new EnergyComp(2));
                def.add(new EnergyRegenComp(2 / lfps));

                def.add(new RangeComp(8));
                def.add(new DamageComp(1));
                def.add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                def.add(new TargetComp());
                def.add(new CooldownComp(60f));

                def.add(new PartTextureComp("b-pencil"));

                def.add(new BuildTypeComp(BuildTypeComp.GROUP_DAMAGE));
            }
        };
    }

    public static void createUnit(){
        unit = new EntityType("unit"){
            {
                groups.add("unit");
                def.add(new PositionComp());
                def.add(new HitboxComp(1));
            }
        };

        eraser = new EntityType("eraser", unit){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.ALL));
                def.add(new TeamComp(1));

                def.add(new HealthComp(8));

                def.add(new MoveSpeedComp(1f / lfps));
                def.add(new VelocityComp());
                def.add(new MovementNextPathComp());

                def.add(new PartTextureComp("u-eraser"));
            }
        };

        tileStain = new EntityType("tileStain", unit){
            {
                groups.add("tileStain");
                def.add(new TileStainComp());
                def.add(new CollideComp(CollideComp.OVERLAY, true).setCollidesMask(CollideComp.ENTITY));

                def.add(new TeamComp(0));
                def.add(new HealthComp(-1));
            }
        };
    }

    public static void create(){
        createUnit();
        createBuilding();
    }
}
