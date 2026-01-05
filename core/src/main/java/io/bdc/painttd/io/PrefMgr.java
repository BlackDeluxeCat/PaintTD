package io.bdc.painttd.io;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;

/**
 * Json实现的首选项管理器.
 */
public class PrefMgr {


    public FileHandle file = Core.gameDataFolder.child("prefs.json");

    public ObjectMap<String, Object> prefs;
    public ObjectMap<String, Object> defaults = new ObjectMap<>();

    public boolean changed;

    public PrefMgr() {

    }

    public void clear() {
        prefs.clear();
        changed = true;
    }

    public void put(String key, Object value) {
        prefs.put(key, value);
        changed = true;
    }

    public void putInt(String key, int value) {
        put(key, value);
    }

    public void putFloat(String key, float value) {
        put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        put(key, value);
    }

    public void putLong(String key, long value) {
        put(key, value);
    }

    public void putDouble(String key, double value) {
        put(key, value);
    }

    public void setDef(String key, Object value) {
        defaults.put(key, value);
    }

    public void setDefBool(String key, boolean def) {
        setDef(key, def);
    }

    public void setDefInt(String key, int def) {
        setDef(key, def);
    }

    public void setDefFloat(String key, float def) {
        setDef(key, def);
    }

    public void setDefLong(String key, long def) {
        setDef(key, def);
    }

    public void setDefDouble(String key, double def) {
        setDef(key, def);
    }

    public void setDefString(String key, String def) {
        setDef(key, def);
    }

    public @Null Object get(String key) {
        Object value = prefs.get(key);
        if (value == null) {
            value = defaults.get(key);
        }
        return value;
    }

    // 带默认参数的get方法
    public int getInt(String key, int def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefInt(key, def);
                return def;
            } else {
                return (int)d;
            }
        }
        return (int)v;
    }

    public String getString(String key, String def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefString(key, def);
                return def;
            } else {
                return (String)d;
            }
        }
        return (String)v;
    }

    public boolean getBoolean(String key, boolean def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefBool(key, def);
                return def;
            } else {
                return (boolean)d;
            }
        }
        return (boolean)v;
    }

    public float getFloat(String key, float def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefFloat(key, def);
                return def;
            } else {
                return (float)d;
            }
        }
        return (float)v;
    }

    public long getLong(String key, long def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefLong(key, def);
                return def;
            } else {
                return (long)d;
            }
        }
        return (long)v;
    }

    public double getDouble(String key, double def) {
        Object v = get(key);
        if (v == null) {
            Object d = defaults.get(key);
            if (d == null) {
                setDefDouble(key, def);
                return def;
            } else {
                return (double)d;
            }
        }
        return (double)v;
    }

    // 不带默认参数的get方法
    public int getInt(String key) {
        return getInt(key, 0);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public void load() {
        try {
            if (file.exists()) {
                prefs = JsonIO.json.fromJson(ObjectMap.class, file.readString());
            } else {
                prefs = new ObjectMap<>();
            }
        } catch (Exception e) {
            Gdx.app.error("PrefMgr", "Failed to load prefs.", e);
            prefs = new ObjectMap<>();
        }
    }

    public void save() {
        if (changed) {
            file.writeString(JsonIO.json.prettyPrint(JsonIO.json.toJson(prefs)), false);
            Gdx.app.debug("PrefMgr", prefs.size + " prefs saved.");
            changed = false;
        }
    }
}
