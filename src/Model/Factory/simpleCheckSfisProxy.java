/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Model.Interface.IPrepare;
import View.DrawBoardUI.UIWarehouse.AbsProxy;

/**
 *
 * @author Administrator
 */
public class simpleCheckSfisProxy extends AbsProxy<IPrepare> {

    public simpleCheckSfisProxy(String type) {
        super(type);
    }

    @Override
    public IPrepare takeIt() {
        return new SimpleFsis();
    }
    
}
