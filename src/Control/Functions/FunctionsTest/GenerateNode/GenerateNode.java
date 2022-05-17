/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.GenerateNode;

import Control.Functions.AbsFunction;

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
                System.out.println(getItemName() + i);
                this.addLog(i);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        this.setRsutlt("PASS");
        return true;
    }

    
}
