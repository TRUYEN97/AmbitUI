/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandTelnet;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeMs;
import Communicate.Telnet.Telnet;
import Model.DataTest.FunctionParameters;

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
        String ip = this.analysisBase.getIp();
        Telnet telnet = null;
        try {
            if (ip == null || (telnet = this.functionBase.getTelnet(ip, 23)) == null
                    || !this.functionBase.sendCommand(telnet, allConfig.getString("command"))) {
                return false;
            }
            String startkey = allConfig.getString("Startkey");
            String endkey = allConfig.getString("Endkey");
            String regex = allConfig.getString("Regex");
            String value = this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeMs(1000));
            if (value == null) {
                return false;
            }
            setResult(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            this.functionBase.disConnect(telnet);
        }
    }

}
