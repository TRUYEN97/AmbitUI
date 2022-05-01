/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.UIWarehouse;

import View.subUI.SubUI.AbsSubUi;
import View.subUI.SubUI.BigUI;

/**
 *
 * @author Administrator
 */
public class BigUIProxy extends AbsProxy<AbsSubUi> {

    public BigUIProxy(String type) {
        super(type);
    }

    @Override
    public AbsSubUi takeIt() {
        if (getName() == null) {
            return null;
        }
        return new BigUI(getName());
    }

}
