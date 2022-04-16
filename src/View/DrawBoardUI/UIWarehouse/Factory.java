/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.UIWarehouse;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 * @param <T>
 */
public class Factory<T> {

    private final Map<String, AbsProxy<T>> type;
    private static volatile Factory instance;

    private Factory() {
        this.type = new HashMap<>();
    }

    public static Factory getInstance() {
        Factory ins = Factory.instance;
        if (ins == null) {
            synchronized (Factory.class) {
                ins = Factory.instance;
                if (ins == null) {
                    Factory.instance = ins = new Factory();
                }
            }
        }
        return ins;
    }

    public void addType(AbsProxy proxyUi) {
        this.type.put(proxyUi.getTypeName(), proxyUi);
    }

    public T getUIType(String type, String IndexName) {
        if (!this.type.containsKey(type)) {
            JOptionPane.showMessageDialog(null, String.format("Not have: %s in factory !", type));
            return null;
        }
        AbsProxy<T> proxy = this.type.get(type);
        proxy.setName(IndexName);
        return proxy.takeIt();
    }
}
