/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.PowerSwitch;

import Control.Functions.AbsFunction;
import Model.DataTest.ErrorLog;
import commandprompt.Communicate.PowerSwitch.PowerSwitch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PowerSwitchFunc extends AbsFunction {

    private PowerSwitch powerSwitch;

    public PowerSwitchFunc(String functionName) {
        super(functionName);
    }

    @Override
    protected boolean test() {
        try {
            this.powerSwitch = new PowerSwitch(this.allConfig.getString("host"),
                    this.allConfig.getString("user"), this.allConfig.getString("password"));
            int times = this.allConfig.getInteger("Times");
            for (int i = 0; i < times; i++) {
            addLog(String.format("cycleTime: %d - %d ",i, times));
                if(onOff(1,1000)){
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

    private boolean onOff(int index, int delay) {
        try {
            if (!this.powerSwitch.setOff(index)) {
                addLog(this.powerSwitch.getResult());
                return false;
            }
            addLog(this.powerSwitch.getResult());
            Thread.sleep(delay);
            if (!this.powerSwitch.setOn(index)) {
                addLog(this.powerSwitch.getResult());
                return false;
            }
            addLog(this.powerSwitch.getResult());
            Thread.sleep(delay);
            return true;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            addLog(ex.getMessage());
            return false;
        }
    }

}
