/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.InitPackages.InitProxy;

import Control.Functions.AbsFunction;
import Control.Functions.InitPackages.idPassWord;
import View.subUI.UiProxy.AbsProxy;
import Model.Interface.IFunction;

/**
 *
 * @author Administrator
 */
public class IdPasswordProxy extends AbsProxy<IFunction>{

    public IdPasswordProxy(String type) {
        super(type);
    }

    @Override
    public IFunction takeIt() {
        return new idPassWord("1", "1");
    }
    
}
