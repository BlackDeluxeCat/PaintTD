package io.blackdeluxecat.painttd.map;

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

    public void addColor(int color){
        colors.add(color);
    }

    public boolean setColor(int index, int color){
        if(index < 0 || index >= colors.size){
            return false;
        }

        colors.set(index, color);
        return true;
    }

    public int unsafeGetColor(int index){
        return colors.get(index);
    }

    public int getColor(int index){
        return unsafeGetColor(MathUtils.clamp(index, 0, colors.size - 1));
    }
}
