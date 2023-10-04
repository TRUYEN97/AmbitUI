/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.PowerSwicthPing;

import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Runin.PowerSwitch.PowerSwitchFunc;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class PowerSwitchPing extends PowerSwitchFunc {

    private final FunctionBase baseFunc;

    public PowerSwitchPing(FunctionParameters parameters) {
        this(parameters, null);
    }

    public PowerSwitchPing(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
    }

    @Override
    public boolean doSomethings() {
        try {
            String ip = this.baseFunc.getIp();
            int time = this.config.getInteger("time_ping", 120);
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            return this.baseFunc.pingTo(ip, time);
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

}
