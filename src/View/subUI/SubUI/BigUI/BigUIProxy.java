/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.SubUI.BigUI;

import Model.Factory.AbsProxy;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author Administrator
 */
public class BigUIProxy extends AbsProxy<AbsSubUi> {

    public BigUIProxy() {
    }

    @Override
    public AbsSubUi takeIt() {
        if (getName() == null) {
            return null;
        }
        return new BigUI(getName());
    }

}
