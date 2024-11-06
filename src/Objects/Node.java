package Objects;

public class Node {


    private Node parent;
    final private float[] position;
    final private float[] rotation;

    public Node() {
        this.position = new float[3];
        this.rotation = new float[3];

    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setPosition(float x, float y, float z) {
        this.position[0] = x;
        this.position[1] = y;
        this.position[2] = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation[0] = x;
        this.rotation[1] = y;
        this.rotation[2] = z;
    }

    public float[] getPosition() {
        return this.position;
    }

    public float[] getRotation() {
        return this.rotation;
    }

    public void move(float x, float y, float z) {
        this.position[0] += x;
        this.position[1] += y;
        this.position[2] += z;
    }

    public void rotate(float x, float y, float z) {
        this.rotation[0] += x;
        this.rotation[1] += y;
        this.rotation[2] += z;
    }

    public void getGlobalPosition(float[] pos) {
        pos[0] = this.position[0];
        pos[1] = this.position[1];
        pos[2] = this.position[2];

        Node current = this.parent;
        while(current != null) {
            pos[0] += current.position[0];
            pos[1] += current.position[1];
            pos[2] += current.position[2];
            current = current.parent;
        }
    }

    public void getGlobalRotation(float[] rot) {
        rot[0] = this.rotation[0];
        rot[1] = this.rotation[1];
        rot[2] = this.rotation[2];

        Node current = this.parent;
        while(current != null) {
            rot[0] += current.rotation[0];
            rot[1] += current.rotation[1];
            rot[2] += current.rotation[2];
            current = current.parent;
        }
    }

    public float getGlobalPositionX() {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        return pos[0];
    }

    public float getGlobalPositionY() {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        return pos[1];
    }

    public float getGlobalPositionZ() {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        return pos[2];
    }

    public float getGlobalRotationX() {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        return rot[0];
    }

    public float getGlobalRotationY() {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        return rot[1];
    }

    public float getGlobalRotationZ() {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        return rot[2];
    }

    public void setGlobalPosition(float x, float y, float z) {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        this.position[0] += x - pos[0];
        this.position[1] += y - pos[1];
        this.position[2] += z - pos[2];
    }

    public void setGlobalRotation(float x, float y, float z) {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        this.rotation[0] += x - rot[0];
        this.rotation[1] += y - rot[1];
        this.rotation[2] += z - rot[2];
    }

    public void setGlobalPositionX(float x) {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        this.position[0] += x - pos[0];
    }

    public void setGlobalPositionY(float y) {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        this.position[1] += y - pos[1];
    }

    public void setGlobalPositionZ(float z) {
        float[] pos = new float[3];
        getGlobalPosition(pos);
        this.position[2] += z - pos[2];
    }

    public void setGlobalRotationX(float x) {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        this.rotation[0] += x - rot[0];
    }

    public void setGlobalRotationY(float y) {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        this.rotation[1] += y - rot[1];
    }

    public void setGlobalRotationZ(float z) {
        float[] rot = new float[3];
        getGlobalRotation(rot);
        this.rotation[2] += z - rot[2];
    }

    public float getPositionX() {
        return this.position[0];
    }

    public float getPositionY() {
        return this.position[1];
    }

    public float getPositionZ() {
        return this.position[2];
    }

    public float getRotationX() {
        return this.rotation[0];
    }

    public float getRotationY() {
        return this.rotation[1];
    }

    public float getRotationZ() {
        return this.rotation[2];
    }

    public void setPositionX(float x) {
        this.position[0] = x;
    }

    public void setPositionY(float y) {
        this.position[1] = y;
    }

    public void setPositionZ(float z) {
        this.position[2] = z;
    }

    public void setRotationX(float x) {
        this.rotation[0] = x;
    }

    public void setRotationY(float y) {
        this.rotation[1] = y;
    }

    public void setRotationZ(float z) {
        this.rotation[2] = z;
    }




}
