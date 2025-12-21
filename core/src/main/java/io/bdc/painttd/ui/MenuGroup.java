package io.bdc.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.game.Game;
import io.bdc.painttd.game.path.*;
import io.bdc.painttd.io.*;

public class MenuGroup extends Table {
    public Table title;
    public Table buttons;

    public void create() {
        clear();
        setBackground(Styles.menu);
        UI.stage.addActor(this);
        setFillParent(true);
        ActorUtils.setVisible(this, t -> UI.inMenu);
        title = new Table();
        buttons = new Table();
        add(title).growX().height(300f).row();
        add(buttons).top();

        title.add(new ActorUtils<>(new Label("Paint TD", Styles.sLabel)).with(l -> {
            l.setFontScale(3);
            l.setAlignment(Align.center);
        }).actor);

        buttons.pad(20);
        buttons.defaults().growX().space(20);
        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.new"), Styles.sTextB))
                             .click(b -> {
                                 UI.inGame = true;
                                 UI.inMenu = false;
                                 Game.createNewMap();
                             })
                             .actor).row();

        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.custom"), Styles.sTextB))
                             .click(b -> {

                             })
                             .actor).row();

        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.load"), Styles.sTextB))
                             .click(b -> {
                                 UI.inGame = true;
                                 UI.inMenu = false;
                                 SaveHandler.load("save0");
                             })
                             .actor).row();

        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.testNodeGraphEditor"), Styles.sTextB))
                             .click(b -> {
                                 UI.nodeGraphEditorDialog.show(new NodeGraph());
                             })
                             .actor).row();

        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.pref"), Styles.sTextB))
                             .click(b -> {
                                 UI.preferenceDialog.show();
                             })
                             .actor).row();

        buttons.add(ActorUtils.wrapper
                             .set(new TextButton(Core.i18n.get("menu.exit"), Styles.sTextB))
                             .click(b -> {
                                 Gdx.app.exit();
                             })
                             .actor).row();
    }
}
