/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.OpenShortOnOff;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.MBLT.OpenShort.OpenShort;
import Control.Functions.FunctionsTest.MBLT.UsbAside.UsbAside;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class OpenShortOnOff extends AbsFunction {

    private final UsbAside aside;
    private final OpenShort openShort;

    public OpenShortOnOff(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public OpenShortOnOff(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.aside = new UsbAside(parameters, item);
        this.openShort = new OpenShort(parameters, item);
    }

    @Override
    public boolean test() {
        try {
            int times = this.allConfig.getInteger("CycleTimes",1);
            addLog("CONFIG", "Times: " + times);
            for (int i = 1; i <= times; i++) {
                addLog(String.format("cycle Times: %d - %d ", i, times));
                if (!aside.test() || !this.openShort.test()) {
                    return false;
                }
                Thread.sleep(1000);
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
