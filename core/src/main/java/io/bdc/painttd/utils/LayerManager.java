package io.bdc.painttd.utils;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.utils.func.*;


public class LayerManager<T>{
    public final Array<Layer<T>> layers = new Array<>();

    public Layer<T> registerLayer(String name, float z){
        Layer<T> l;
        if((l = getLayer(z)) != null){
            Gdx.app.log("LayerSystem", "Layer with z = " + z + " already exists.");
            return l;
        }
        if((l = getLayer(name)) != null){
            Gdx.app.log("LayerSystem", "Layer with name " + name + " already exists.");
            return l;
        }

        l = new Layer<>(name, z);
        layers.add(l);
        return l;
    }

    public @Null Layer<T> getLayer(String name){
        return IterateUtils.first(layers, l -> l.name.equals(name));
    }

    public @Null Layer<T> getLayer(float z){
        return IterateUtils.first(layers, l -> l.z == z);
    }

    public @Null Layer<T> getLayer(T obj){
        return IterateUtils.first(layers, l -> l.objects.contains(obj, true));
    }

    public float getZ(T obj){
        Layer<T> l = getLayer(obj);
        return l != null ? l.z : 0;
    }

    /**
     * 线程不安全
     */
    public void sort(){
        layers.sort();
    }

    public static class Layer<A> implements Comparable<Layer<A>>{
        public final String name;
        /**
         * 一旦修改, 必须排序LayerManager.
         */
        protected float z;
        public Array<A> objects = new Array<>();

        public Layer(String name, float z){
            this.name = name;
            this.z = z;
        }

        public boolean add(A obj){
            if(objects.contains(obj, true)) return false;
            objects.add(obj);
            return true;
        }

        public void with(Cons<Layer<A>> cons){
            cons.get(this);
        }

        public float z(){
            return z;
        }

        public void z(float z){
            this.z = z;
        }

        public int count(){
            return objects.size;
        }

        @Override
        public int compareTo(Layer<A> o){
            return Float.compare(z, o.z);
        }
    }
}
