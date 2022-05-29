/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.UiProxy;

import View.subUI.FormDetail.AbsTabUI;
import View.subUI.FormDetail.TabItem;

/**
 *
 * @author Administrator
 */
public class TabItemProxy extends AbsProxy<AbsTabUI>{

    
    public TabItemProxy() {
    }

    @Override
    public AbsTabUI takeIt() {
        return new TabItem();
    }
    
}
