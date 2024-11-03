package Inputs;

import java.util.HashMap;

public class KeyMap {

    private HashMap<String, Integer> mapping;
    private boolean[] keys;

    public KeyMap(){
        mapping = new HashMap<>();
        keys = new boolean[256];
    }

    public void add(String name, int key){
        mapping.put(name, key);
    }

    public void released(int key){
        keys[key] = false;
    }

    public void pressed(int key){
        keys[key] = true;
    }

    public boolean is(String map){
        int key = mapping.getOrDefault(map, -1);
        if(key < 0) return false;
        return keys[key];
    }
}
