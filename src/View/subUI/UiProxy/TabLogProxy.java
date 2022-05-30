/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.UiProxy;

import View.subUI.FormDetail.AbsTabUI;
import View.subUI.FormDetail.Tablog;

/**
 *
 * @author Administrator
 */
public class TabLogProxy extends AbsProxy<AbsTabUI> {

    public TabLogProxy() {
    }

    @Override
    public AbsTabUI takeIt() {
        if (getTypeName() == null) {
            return null;
        }
        return new Tablog(getName());
    }

}
