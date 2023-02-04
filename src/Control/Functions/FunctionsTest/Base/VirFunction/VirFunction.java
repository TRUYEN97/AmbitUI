/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.VirFunction;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class VirFunction extends AbsFunction{

    public VirFunction(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }
    
    public VirFunction(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        return true;
    }
    
}
