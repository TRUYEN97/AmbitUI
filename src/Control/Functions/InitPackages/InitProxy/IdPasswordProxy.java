/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.InitPackages.InitProxy;

import Control.Functions.InitPackages.idPassWord;
import Model.Factory.AbsProxy;
import Model.Interface.IFunction;

/**
 *
 * @author Administrator
 */
public class IdPasswordProxy extends AbsProxy<IFunction>{

    public IdPasswordProxy() {
    }

    @Override
    public IFunction takeIt() {
        return new idPassWord("1", "1");
    }
    
}
