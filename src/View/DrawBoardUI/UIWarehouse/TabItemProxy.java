/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.UIWarehouse;

import View.DrawBoardUI.FormDetail.AbsTabUI;
import View.DrawBoardUI.FormDetail.TabItem;

/**
 *
 * @author Administrator
 */
public class TabItemProxy extends AbsProxy<AbsTabUI>{

    
    public TabItemProxy(String type) {
        super(type);
    }

    @Override
    public AbsTabUI takeIt() {
        return new TabItem();
    }
    
}
