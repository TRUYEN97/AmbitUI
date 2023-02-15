/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckUpdate;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class CheckUpdate extends AbsFunction{

    public CheckUpdate(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }
    
    public CheckUpdate(FunctionParameters functionParameters) {
        super(functionParameters, null);
    }
    

    @Override
    protected boolean test() {
        return true;
    }
    
}
