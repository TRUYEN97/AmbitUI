/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.FormDetail.TabFaApi;

import Model.Factory.AbsProxy;
import View.subUI.FormDetail.AbsTabUI;

/**
 *
 * @author Administrator
 */
public class TabFaApiProxy extends AbsProxy<String, AbsTabUI> {

    @Override
    public AbsTabUI takeIt() {
        return new TabFaApi(getTypeName());
    }
    
}
