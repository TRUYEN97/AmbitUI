/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.UIWarehouse;

import View.DrawBoardUI.FormDetail.AbsTabUI;
import View.DrawBoardUI.FormDetail.TabView;

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
