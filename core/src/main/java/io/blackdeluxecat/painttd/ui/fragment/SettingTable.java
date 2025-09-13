package io.blackdeluxecat.painttd.ui.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.blackdeluxecat.painttd.ui.ActorUtils;

import static io.blackdeluxecat.painttd.ui.Styles.*;

public class SettingTable extends Table {

    public Table settingList = new Table();
    public boolean isOpen=false;

    //下面为参数列表
    public static boolean mouseReverse = false;


    public SettingTable(){
        create();
    }

    public void create(){
        //先关闭
        settingList.setVisible(isOpen);
        isOpen = !isOpen;

        this.add(ActorUtils.wrapper.set(new TextButton("设置",sTextB))
            .click(b -> {
                settingList();
            })
            .actor);

        settingList.add(ActorUtils.wrapper.set(new TextButton("鼠标滚轮反转",checkBox))
            .click(
                b -> {
                    mouseReverse = !mouseReverse;
                }).actor);

    }

    public void settingList() {

        System.out.println("setting button is clicked");
        settingList.setVisible(isOpen);
        isOpen = !isOpen;
    }
}
