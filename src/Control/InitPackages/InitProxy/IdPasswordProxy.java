/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.InitPackages.InitProxy;

import Control.InitPackages.idPassWord;
import Model.Interface.IInit;
import View.DrawBoardUI.UIWarehouse.AbsProxy;

/**
 *
 * @author Administrator
 */
public class IdPasswordProxy extends AbsProxy<IInit>{

    public IdPasswordProxy(String type) {
        super(type);
    }

    @Override
    public IInit takeIt() {
        return new idPassWord("sqt", "123");
    }
    
}
