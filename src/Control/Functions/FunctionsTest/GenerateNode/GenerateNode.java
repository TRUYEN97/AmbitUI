/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.GenerateNode;

import Control.Functions.AbsFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 21AK22
 */
public class GenerateNode extends AbsFunction{

    public GenerateNode(String FunctionName) {
        super(FunctionName);
    }

    @Override
    public boolean test() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(i);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    
}
