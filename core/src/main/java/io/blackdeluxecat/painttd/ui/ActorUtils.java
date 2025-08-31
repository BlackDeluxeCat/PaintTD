package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.blackdeluxecat.painttd.struct.func.*;

/**
 * Actor元素的包装类, 提供更多链式调用.
 * 对类型不严格的场景, 可以复用静态的{@link #wrapper}
 * 频繁更新的场景, 可以复用自行构造的单例
 */
public class ActorUtils<T extends Actor>{
    public static ActorUtils<Actor> wrapper = new ActorUtils<>();

    public T actor;

    protected ActorUtils(){}

    public ActorUtils(T actor){
        this.actor = actor;
    }

    public ActorUtils<T> set(T actor){
        this.actor = actor;
        return this;
    }

    public ActorUtils<T> update(Cons<T> updater){
        ActorUtils.updater(actor, updater);
        return this;
    }

    public ActorUtils<T> click(Cons<T> clicked){
        ActorUtils.clicked(actor, clicked);
        return this;
    }

    public ActorUtils<T> with(Cons<T> cons){
        cons.get(actor);
        return this;
    }

    public static <T extends Actor> void updater(T actor, Cons<T> updater){
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> updater.get(actor));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);
    }

    public static <T extends Actor> void clicked(T actor, Cons<T> clicked){
        actor.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                clicked.get(actor);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y){
            }
        });
    }
}
