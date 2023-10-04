/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutTelnet;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class DutTelnet extends AbsFunction {

    private final FunctionBase functionBase;

    public DutTelnet(FunctionParameters parameters) {
        this(parameters, null);
    }

    public DutTelnet(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String ip = this.functionBase.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            try ( Telnet telnet = this.functionBase.getTelnet(ip, 23)) {
                return telnet != null;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLog.addError(this, e.getMessage());
                return false;
            }
        } catch (Exception ex) {
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }
}
