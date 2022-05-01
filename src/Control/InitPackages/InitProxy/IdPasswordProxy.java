/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.InitPackages.InitProxy;

import Control.Functions.AbsFunction;
import Control.InitPackages.idPassWord;
import View.subUI.UIWarehouse.AbsProxy;

/**
 *
 * @author Administrator
 */
public class IdPasswordProxy extends AbsProxy<AbsFunction>{

    public IdPasswordProxy(String type) {
        super(type);
    }

    @Override
    public AbsFunction takeIt() {
        return new idPassWord("1", "1");
    }
    
}
