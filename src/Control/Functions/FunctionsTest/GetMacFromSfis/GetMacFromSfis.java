/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.GetMacFromSfis;

import Control.Functions.AbsFunction;

/**
 *
 * @author Administrator
 */
public class GetMacFromSfis extends AbsFunction {

    public GetMacFromSfis(String FunctionName) {
        super(FunctionName);
    }

    @Override
    public boolean test() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(getItemName() + i);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        setRsutlt("Pass");
        return true;
    }

}
