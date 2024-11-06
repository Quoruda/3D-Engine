package Portal;

import Inputs.Controller;
import Inputs.KeyMap;
import Objects.Camera;

public class FreeCam extends Camera implements Controller {

    float speed = 2f;

    @Override
    public void update(KeyMap keyMap, float deltaTime) {
        float v = speed*deltaTime;
        if(keyMap.is("lookRight")){
            this.rotateYaw(v);
        }
        if(keyMap.is("lookLeft")){
            this.rotateYaw(-v);
        }
        if(keyMap.is("lookUp")){
            this.rotatePitch(v);
        }
        if(keyMap.is("lookDown")){
            this.rotatePitch(-v);
        }
        v = 5f*v;
        if(keyMap.is("forward")){
            this.moveForward(v);
        }
        if(keyMap.is("backward")){
            this.moveForward(-v);
        }
        if(keyMap.is("right")){
            this.moveRight(v);
        }
        if(keyMap.is("left")){
            this.moveRight(-v);
        }
    }

}
