/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.TelnetReadUntilKey;

import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class TelnetReadUntilKeyProxy extends AbsProxy<AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new TelnetReadUntilKey(getName());
    }
    
}
