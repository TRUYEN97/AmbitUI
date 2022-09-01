/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.SubUI.SmallUI;

import Model.Factory.AbsProxy;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author Administrator
 */
public class SmallUIProxy extends AbsProxy<String, AbsSubUi> {

    public SmallUIProxy() {
    }

    @Override
    public AbsSubUi takeIt() {
        if (getID() == null) {
            return null;
        }
        return new SmallUI(getID());
    }

}
