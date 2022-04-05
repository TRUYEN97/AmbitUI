/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.UIWarehouse;

import View.DrawBoardUI.SubUI.AbsSubUi;

/**
 *
 * @author Administrator
 */
public abstract class AbsProxyUI {

    private final String type;

    public AbsProxyUI(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return this.type;
    }

    public abstract AbsSubUi getUI(String indexName);

}
