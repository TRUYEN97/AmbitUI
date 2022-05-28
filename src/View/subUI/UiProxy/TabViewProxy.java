/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.UiProxy;

import View.subUI.FormDetail.AbsTabUI;
import View.subUI.FormDetail.TabView;

/**
 *
 * @author Administrator
 */
public class TabViewProxy extends AbsProxy<AbsTabUI> {

    public TabViewProxy(String type) {
        super(type);
    }

    @Override
    public AbsTabUI takeIt() {
        return new TabView();
    }

}
