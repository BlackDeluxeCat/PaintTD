package io.bdc.painttd.content.components.render;

import com.artemis.annotations.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.content.components.*;

import static io.bdc.painttd.Core.*;

@Transient
public class PartTextureComp extends CopyableComponent{
    public String textureName;
    public TextureAtlas.AtlasRegion atlasRegion;
    public TextureRegionDrawable drawable;

    public PartTextureComp(){
    }

    public PartTextureComp(String textureName){
        this.textureName = textureName;
        this.atlasRegion = atlas.findRegion(textureName);
        this.drawable = new TextureRegionDrawable(atlasRegion);
    }

    @Override
    public PartTextureComp copy(CopyableComponent other){
        PartTextureComp partTextureComp = (PartTextureComp)other;
        this.textureName = partTextureComp.textureName;
        this.atlasRegion = partTextureComp.atlasRegion;
        this.drawable = partTextureComp.drawable;
        return this;
    }

    @Override
    protected void reset(){
        this.textureName = null;
        this.atlasRegion = null;
        this.drawable = null;
    }

    @Override
    public void refill(CopyableComponent def){
        this.copy(def);
    }
}
