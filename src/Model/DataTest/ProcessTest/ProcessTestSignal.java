/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Communicate.IConnect;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author 21AK22
 */
public class ProcessTestSignal {

    private final HashMap<String, Object> signal;

    public ProcessTestSignal() {
        this.signal = new HashMap<>();
    }

    public void put(String key, Object value) {
        this.signal.put(key, value);
    }

    public String getString(String key) {
        return (String) this.signal.get(key);
    }

    public void clear() {
        this.signal.clear();
    }

    public Object getObject(String key) {
        return signal.get(key);
    }

}
