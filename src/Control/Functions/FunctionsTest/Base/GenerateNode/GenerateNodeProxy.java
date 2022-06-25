/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GenerateNode;
import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author 21AK22
 */
public class GenerateNodeProxy extends AbsProxy<AbsFunction>{

    public GenerateNodeProxy() {
    }

    @Override
    public GenerateNode takeIt() {
        return new GenerateNode(getName());
    }
    
}
