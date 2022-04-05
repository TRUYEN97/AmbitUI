/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.UIWarehouse;

import View.DrawBoardUI.SubUI.AbsSubUi;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class FactoryUI {

    private final Map<String, AbsProxyUI> typeUI;
    private static volatile FactoryUI instance;

    private FactoryUI() {
        this.typeUI = new HashMap<>();
    }

    public static FactoryUI getInstance() {
        FactoryUI ins = FactoryUI.instance;
        if (ins == null) {
            synchronized (FactoryUI.class) {
                ins = FactoryUI.instance;
                if (ins == null) {
                    FactoryUI.instance = ins = new FactoryUI();
                }
            }
        }
        return ins;
    }

    public void addType(AbsProxyUI proxyUi) {
        this.typeUI.put(proxyUi.getTypeName(), proxyUi);
    }

    public AbsSubUi getUIType(String type, String IndexName) {
        if (!this.typeUI.containsKey(type)) {
            return null;
        }
        return typeUI.get(type).getUI(IndexName);
    }
}
