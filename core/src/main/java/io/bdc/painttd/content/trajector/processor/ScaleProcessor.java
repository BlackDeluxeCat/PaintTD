package io.bdc.painttd.content.trajector.processor;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.var.*;

public class ScaleProcessor extends Processor{
    public PortFloat inputX, inputY;
    public PortVector2 input;

    public PortFloat scaleX, scaleY;

    public ScaleProcessor(){
        super(3);
    }

    @Override
    public void registerVars(){
        super.registerVars();
        inputX = new PortFloat("inputX", 0);
        inputY = new PortFloat("inputY", 1);
        input = new PortVector2("input"){
            @Override
            public void set(Vector2 value, Node node){
                inputX.setFloat(value.x, node);
                inputY.setFloat(value.y, node);
            }

            @Override
            public Vector2 get(Node node){
                return tmp.set(inputX.asFloat(node), inputY.asFloat(node));
            }
        };
        scaleX = new PortFloat("scaleX", 2);
        scaleY = new PortFloat("scaleY", 3);

        inputs.add(input);
        inputs.add(scaleX);
        inputs.add(scaleY);
    }

    @Override
    public void initial(Node node){
        super.initial(node);
        input.set(Vector2.Zero, node);
        scaleX.setFloat(2, node);
        scaleY.setFloat(2, node);
    }

    @Override
    public void update(float frame, Node node){
        updateInputLinks(frame, node);
        node.shift.set(input.get(node).scl(scaleX.asFloat(node), scaleY.asFloat(node)));
    }
}
