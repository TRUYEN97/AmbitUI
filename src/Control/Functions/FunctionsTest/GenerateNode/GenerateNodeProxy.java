/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.GenerateNode;
import Control.Functions.AbsFunction;
import View.subUI.UIWarehouse.AbsProxy;

/**
 *
 * @author 21AK22
 */
public class GenerateNodeProxy extends AbsProxy<AbsFunction>{

    public GenerateNodeProxy(String type) {
        super(type);
    }

    @Override
    public GenerateNode takeIt() {
        return new GenerateNode(getName());
    }
    
}
