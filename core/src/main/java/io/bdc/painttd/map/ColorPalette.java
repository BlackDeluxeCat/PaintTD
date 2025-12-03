package io.bdc.painttd.map;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class ColorPalette{
    public IntArray colors;

    public ColorPalette(){
        colors = new IntArray();
    }

    public int size(){
        return colors.size;
    }

    public void addColor(Color color){
        colors.add(Color.rgba8888(color));
    }

    public boolean setColor(int index, Color color){
        if(index < 0 || index >= colors.size){
            return false;
        }

        colors.set(index, Color.rgba8888(color));
        return true;
    }

    public int unsafeGetColor(int index){
        return colors.get(index);
    }

    public Color getColor(Color out, int index){
        return out.set(unsafeGetColor(MathUtils.clamp(index, 0, colors.size - 1)));
    }
}
