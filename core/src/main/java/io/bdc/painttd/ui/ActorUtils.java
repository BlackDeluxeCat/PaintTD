package io.bdc.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.utils.func.*;

/**
 * Actor元素的包装类, 提供更多链式调用.
 * 对类型不严格的场景, 可以复用静态的{@link #wrapper}
 * 频繁更新的场景, 可以复用自行构造的单例
 */
public class ActorUtils<T extends Actor> {
    public static ActorUtils<Actor> wrapper = new ActorUtils<>();

    public T actor;

    protected ActorUtils() {
    }

    public ActorUtils(T actor) {
        this.actor = actor;
    }

    public ActorUtils<T> set(T actor) {
        this.actor = actor;
        return this;
    }

    public ActorUtils<T> update(Cons<T> updater) {
        ActorUtils.updater(actor, updater);
        return this;
    }

    public ActorUtils<T> visible(Boolf<T> visible) {
        ActorUtils.setVisible(actor, visible);
        return this;
    }

    public ActorUtils<T> click(Cons<T> clicked) {
        ActorUtils.clicked(actor, clicked);
        return this;
    }

    public ActorUtils<T> with(Cons<T> cons) {
        cons.get(actor);
        return this;
    }

    public static <T extends Actor> T updater(T actor, Cons<T> updater) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> updater.get(actor));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);

        return actor;
    }

    public static <T extends Actor> T setVisible(T actor, Boolf<T> visible) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> actor.setVisible(visible.get(actor)));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);

        return actor;
    }

    public static <T extends Actor> T clicked(T actor, Cons<T> clicked) {
        actor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clicked.get(actor);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        return actor;
    }

    /**
     * 查询元素是否存在符合条件的父母
     */
    public static boolean isDescendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return true;
            parent = parent.getParent();
        } while (parent != null);
        return false;
    }

    /**
     * 查询元素是否存在符合条件的子元素
     */
    public static boolean isAscendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return true;
            parent = parent.getParent();
        } while (parent != null);
        return false;
    }

    /**
     * 取得符合条件的父母. 总是返回第一个符合的元素.
     */
    public static Actor findDescendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return parent;
            parent = parent.getParent();
        } while (parent != null);
        return null;
    }

    /**
     * 取得符合条件的子元素. 总是返回第一个符合的元素.
     */
    public static Actor findAscendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return parent;
            parent = parent.getParent();
        } while (parent != null);
        return null;
    }

    public static TextField.TextFieldFilter
        digitOnly = new TextField.TextFieldFilter.DigitsOnlyFilter(),
        floatOnly = (textField, c) -> Character.isDigit(c) || (textField.getText().contains(".") && c == '.');
}
