/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import View.subUI.UIWarehouse.AbsProxy;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 * @param <T>
 */
public class FactoryType<T> {

    private final Map<String, AbsProxy<T>> type;

    public FactoryType() {
        this.type = new HashMap<>();
    }

    public void addType(AbsProxy<T> proxyUi) {
        this.type.put(proxyUi.getTypeName(), proxyUi);
    }

    public T takeIt(String type, String name) {
        if (!this.type.containsKey(type)) {
            JOptionPane.showMessageDialog(null, String.format("Not have: %s in factory !", type));
            return null;
        }
        AbsProxy<T> proxy = this.type.get(type);
        proxy.setName(name);
        return proxy.takeIt();
    }

    public T takeIt(String type) {
        return FactoryType.this.takeIt(type, null);
    }
}
