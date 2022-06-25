/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GetMacFromSfis;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;

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
        try {
            String mac = this.uiData.getProductInfo(AllKeyWord.MAC);
            if (mac == null || mac.length() != 12) {
                addLog("MAC is invalid: " + mac);
                return false;
            }
            addLog("Get mac= " + mac);
            return true;
        } finally {
            addLog("MAC range 100000000000-ffffffffffff");
        }
    }

}
