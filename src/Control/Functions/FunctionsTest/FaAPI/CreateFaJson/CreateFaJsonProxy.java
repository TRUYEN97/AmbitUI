/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.FaAPI.CreateFaJson;

import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class CreateFaJsonProxy extends AbsProxy<AbsFunction>{

    public CreateFaJsonProxy() {
    }

    
    @Override
    public AbsFunction takeIt() {
        return new CreateFaJson(getName());
    }
    
}