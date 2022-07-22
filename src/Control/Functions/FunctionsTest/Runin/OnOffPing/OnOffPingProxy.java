/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.OnOffPing;

import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class OnOffPingProxy extends AbsProxy<AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new OnOffPing(getName());
    }
    
}
