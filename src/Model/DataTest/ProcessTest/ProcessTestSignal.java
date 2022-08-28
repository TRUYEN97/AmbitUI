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
    private final Set<IConnect> connector;

    public ProcessTestSignal() {
        this.signal = new HashMap<>();
        this.connector = new HashSet<>();
    }

    public void put(String key, Object value) {
        this.signal.put(key, value);
    }

    public void addConnector(IConnect connect) {
        this.connector.add(connect);
    }

    public boolean disConnectAll() {
        for (IConnect connect : connector) {
            if (!connect.disConnect()) {
                return false;
            }
        }
        this.connector.clear();
        return true;
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

    public boolean disConnect(IConnect connect) {
        if (connect == null || !connect.disConnect()) {
            return false;
        }
        return !this.connector.contains(connect) || this.connector.remove(connect);
    }

}
