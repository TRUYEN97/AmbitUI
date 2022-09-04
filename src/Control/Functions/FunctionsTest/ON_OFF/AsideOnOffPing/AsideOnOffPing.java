/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.AsideOnOffPing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.DutPing.DutPing;
import Control.Functions.FunctionsTest.MBLT.UsbAside.UsbAside;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;

/**
 *
 * @author Administrator
 */
public class AsideOnOffPing extends AbsFunction{

    private final UsbAside aside;
    private final DutPing dutPing;
    
    public AsideOnOffPing(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public AsideOnOffPing(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.aside = new UsbAside(parameters, item);
        this.dutPing = new DutPing(parameters, item);
    }
    

    @Override
    protected boolean test() {
         try {
            int times = this.allConfig.getInteger("CycleTimes",1);
            addLog("CONFIG", "Times: " + times);
            for (int i = 1; i <= times; i++) {
                addLog(String.format("cycle Times: %d - %d ", i, times));
                if (!this.aside.test() || !this.dutPing.test()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            addLog(e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }
    
}
