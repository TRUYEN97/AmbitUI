/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.FixtureAction;

import Control.Functions.AbsFunction;

/**
 *
 * @author Administrator
 */
public class FixtureAction extends AbsFunction {

    public FixtureAction(String FunctionName) {
        super(FunctionName);
    }

    @Override
    public boolean test() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(getItemName() + i);
                Thread.sleep(1000);
                addLog(i);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        this.setRsutlt("PASS");
        return true;
    }

}
