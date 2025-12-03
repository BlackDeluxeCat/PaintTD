package io.bdc.painttd.content.components.logic;

import io.bdc.painttd.content.components.*;

public class TeamComp extends CopyableComponent{
    public int team;

    public TeamComp(){
    }

    public TeamComp(int team){
        this.team = team;
    }

    public boolean isSelf(int other){
        return team == other;
    }

    @Override
    public TeamComp copy(CopyableComponent other){
        if(other instanceof TeamComp) this.team = ((TeamComp)other).team;
        return this;
    }

    @Override
    protected void reset(){
        team = 0;
    }
}
