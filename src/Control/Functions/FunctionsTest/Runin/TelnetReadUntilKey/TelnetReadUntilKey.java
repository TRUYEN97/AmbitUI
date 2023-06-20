/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.TelnetReadUntilKey;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;
import Communicate.Impl.Telnet.Telnet;
import Model.DataTest.FunctionParameters;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TelnetReadUntilKey extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public TelnetReadUntilKey(FunctionParameters parameters) {
        this(parameters, null);
    }

    public TelnetReadUntilKey(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try ( AbsCommunicate communicate = this.baseFunc.getTelnetOrComportConnector()) {
            if (communicate == null) {
                return false;
            }
            return runTest(communicate);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean runTest(AbsCommunicate communicate) {
        List<String> commands = this.config.getJsonList("command");
        if (commands.isEmpty()) {
            addLog("ERROR", "Commands is empty!");
            return false;
        }
        String readUntil = this.config.getString("ReadUntil");
        String spec = this.config.getString("keyWord");
        Integer time = this.config.getInteger("Time");
        if (time == null) {
            addLog("Config", "Time == null !!");
            return false;
        }
        for (String command : commands) {
            addLog("Config", "Waiting for about %s s", time);
            if (!this.baseFunc.sendCommand(communicate, command)
                    || !this.analysisBase.isResponseContainKeyAndShow(communicate, spec, readUntil, new TimeS(time))) {
                if (communicate instanceof Telnet telnet) {
                    try ( Telnet a = this.baseFunc.getTelnet(telnet.getHost(), 23)) {

                    } catch (IOException ex) {
                        addLog(LOG_KEYS.ERROR, ex.getMessage());
                    }
                }
                return false;
            }
        }
        return true;
    }

}
