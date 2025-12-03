package io.bdc.painttd.utils;

import com.badlogic.gdx.utils.*;

public class Time{
    public final static Timer timer = Timer.instance();

    public static long time, last;

    public static void update(){
        last = time;
        time = System.currentTimeMillis();
    }

    public static long delta(){
        return time - last;
    }

    public static long delta(long previousTime){
        return time - previousTime;
    }

    public static void run(Runnable runnable, float delay){
        timer.scheduleTask(new Timer.Task(){
            @Override
            public void run(){
                runnable.run();
            }
        }, delay);
    }
}
