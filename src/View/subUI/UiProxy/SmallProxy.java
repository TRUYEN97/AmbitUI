/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.UiProxy;

import View.subUI.SubUI.AbsSubUi;
import View.subUI.SubUI.SmallUI;

/**
 *
 * @author Administrator
 */
public class SmallProxy extends AbsProxy<AbsSubUi>{

    public SmallProxy(String type) {
        super(type);
    }

    @Override
    public AbsSubUi takeIt() { 
        if (getName() == null) {
            return null;
        }
       return new SmallUI(getName());
    }
    
}
