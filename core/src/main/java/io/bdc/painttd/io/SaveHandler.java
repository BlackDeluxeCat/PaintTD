package io.bdc.painttd.io;

import com.badlogic.gdx.files.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.game.*;
import io.bdc.painttd.game.pathfind.*;

import java.io.*;

public class SaveHandler {
    public static FileHandle path = Core.gameDataFolder.child("save");

    public static void save(String save) {
        SaveFormat format = new SaveFormat(250916, 0);
        format.entities = Game.utils.allEntitiesSub.getEntities();
        format.rules = Game.rules;
        Game.worldSerializationManager.save(getSaveOutput(save), format);
    }

    public static void load(String save) {
        // 重置世界状态
        Game.endMap();

        // 读档, 包括反序列化实体, 反序列化rules
        SaveFormat format = Game.worldSerializationManager.load(getLoadInput(save), SaveFormat.class);
        Game.rules = format.rules;
        Vars.hud.rebuild();
        Game.map.createMap(Game.rules.width, Game.rules.height);
        Game.flowField = new FlowField(Game.map);

        // 读取实体类型并重建完整实体
        var entities = format.entities;
        for (int i = 0; i < entities.size(); i++) {
            var e = Game.world.getEntity(entities.get(i));
            EntityTypes.getById(Game.utils.entityTypeMapper.get(e).type).refill(e);
        }

        // 重建地图瓦片映射
        Game.map.shouldRebindTile = true;
    }

    public static OutputStream getSaveOutput(String fileNameNoExtension) {
        return path.child(fileNameNoExtension + ".json").write(false);
    }

    public static InputStream getLoadInput(String fileNameNoExtension) {
        return path.child(fileNameNoExtension + ".json").read();
    }

    public static boolean exists(String fileNameNoExtension) {
        return path.child(fileNameNoExtension + ".json").exists();
    }
}
