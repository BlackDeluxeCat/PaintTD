package io.blackdeluxecat.painttd.utils.color;

import java.util.LinkedList;


public interface Mixer {
    //两个默认方法
    default void mix(LinkedList<HSVColor> colors){
        for(HSVColor color : colors){
            mixWith(color);
        }
    }

    default void lightenColor(HSVColor color,float amount){
        color.s-= amount;
        //还原到正常数值
        if(color.s < 0) {color.s = 0;}

    }
    void mixWith(HSVColor color);

}
