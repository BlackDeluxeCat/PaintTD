package io.bdc.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;

public class BaseDialog extends Table{
    public Table titleTable;
    public Label titleLabel;

    public Table cont;
    public Table buttons;

    public BaseDialog(String title){
        setFillParent(true);
        pad(40);

        titleLabel = new Label(title, Styles.sLabel);
        titleLabel.setEllipsis(true);
        titleLabel.setAlignment(Align.center);

        titleTable = new Table();
        titleTable.add(titleLabel).growX().row();
        titleTable.add().grow();
        titleTable.setFillParent(true);
        titleTable.pad(40f);
        addActor(titleTable);

        cont = new Table();
        buttons = new Table();

        add(cont).grow().row();
        add(buttons).growX().minHeight(0f);
    }

    public void addCloseButton(){
        buttons.add(ActorUtils.wrapper
                           .set(new TextButton(Core.i18n.get("button.back"), Styles.sTextB))
                           .click(b -> {
                               this.hide();
                           })
                           .actor).height(Styles.buttonSize * 2);
    }

    public void show(){
        Core.stage.addActor(this);
    }

    public void hide(){
        remove();
    }
}
