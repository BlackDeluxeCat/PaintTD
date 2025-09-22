package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.io.*;

public class MenuGroup extends Table{
    public Table title;
    public VerticalGroup buttons;

    public void create(){
        clear();
        setBackground(Styles.menu);
        Core.stage.addActor(this);
        setFillParent(true);
        ActorUtils.setVisible(this, t -> Vars.inMenu);
        title = new Table();
        buttons = new VerticalGroup();
        add(title).growX().height(400f).row();
        add(buttons).grow();

        title.add(new ActorUtils<>(new Label("Paint TD", Styles.sLabel)).with(l -> {
            l.setFontScale(3);
            l.setAlignment(Align.center);
        }).actor);

        buttons.space(10);
        buttons.pad(20);
        buttons.addActor(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.new"), Styles.sTextB))
                             .click(b -> {
                                 Vars.inGame = true;
                                 Vars.inMenu = false;
                                 Game.createNewMap();
                             })
                             .actor);

        buttons.addActor(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.custom"), Styles.sTextB))
                             .click(b -> {

                             })
                             .actor);

        buttons.addActor(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.load"), Styles.sTextB))
                             .click(b -> {
                                 Vars.inGame = true;
                                 Vars.inMenu = false;
                                 SaveHandler.load("save0");
                             })
                             .actor);

        buttons.addActor(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.pref"), Styles.sTextB))
                             .click(b -> {
                                 Vars.preferenceDialog.show();
                             })
                             .actor);

        buttons.addActor(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.exit"), Styles.sTextB))
                             .click(b -> {
                                 Gdx.app.exit();
                             })
                             .actor);
    }
}
