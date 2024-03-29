/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandTelnet;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
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
        try {
            try ( AbsCommunicate communicate = this.functionBase.getTelnetOrComportConnector()) {
                if (communicate == null) {
                    return false;
                }
                if (!this.functionBase.sendCommand(communicate, config.getString("command"))) {
                    return false;
                }
                String startkey = config.getString("Startkey");
                String endkey = config.getString("Endkey");
                String regex = config.getString("Regex");
                int time = config.getInteger("Time", 10);
                String readUntil = config.getString("ReadUntil");
                String value = this.analysisBase.getValue(communicate,
                        startkey, endkey, regex, new TimeS(time), readUntil);
                if (value == null) {
                    return false;
                }
                setResult(value);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

}
