/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED;

import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class MMC_WR_SPEED_PROXY extends AbsProxy<AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new MMC_WR_SPEED(getName());
    }
    
}
