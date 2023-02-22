/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandTelnet;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;

/**
 *
 * @author Administrator
 */
public class CheckCommandTelnet extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public CheckCommandTelnet(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CheckCommandTelnet(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.functionBase.getIp();
        try ( Telnet telnet = this.functionBase.getTelnet(ip, 23)) {
            if (telnet == null) {
                return false;
            }
            if (!this.functionBase.sendCommand(telnet, config.getString("command"))) {
                return false;
            }
            String startkey = config.getString("Startkey");
            String endkey = config.getString("Endkey");
            String regex = config.getString("Regex");
            int time = config.getInteger("Time", 10);
            String value = this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeS(time));
            if (value == null) {
                return false;
            }
            setResult(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

}
