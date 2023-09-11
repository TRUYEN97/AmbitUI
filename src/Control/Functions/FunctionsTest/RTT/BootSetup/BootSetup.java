/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.RTT.BootSetup;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BootSetup extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public BootSetup(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.analysisBase = new AnalysisBase(functionParameters, itemName);
        this.functionBase = new FunctionBase(functionParameters, itemName);
    }

    public BootSetup(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        try ( Telnet telnet = this.functionBase.getTelnet(this.functionBase.getIp())) {
            if (telnet == null) {
                return false;
            }
            List<String> cmds = this.config.getJsonList("command");
            String readUntil = this.config.getString("ReadUntil", "root@eero-test:/#");
            int time = this.config.getInteger("Time", 10);
            if (!sendBootSetupCommands(telnet, cmds, readUntil, time)) {
                return false;
            }
            return checkResponse(telnet, readUntil, time);
        } catch (Exception e) {
            addLog(LOG_KEYS.ERROR, e.getLocalizedMessage());
            return false;
        }
    }

    private boolean checkResponse(final Telnet telnet, String readUntil, int time) {
        String checkCommand = this.config.getString("check_command");
        if (!this.functionBase.sendCommand(telnet, checkCommand)) {
            return false;
        }
        String response = this.analysisBase.readShowUntil(telnet, readUntil, new TimeS(time));
        if (response == null) {
            return false;
        }
        List<String> specs = this.config.getJsonList("spec");
        return this.analysisBase.contains(response, specs);
    }

    private boolean sendBootSetupCommands(final Telnet telnet, List<String> cmds, String readUntil, int time) {
        for (String cmd : cmds) {
            if (!this.functionBase.sendCommand(telnet, cmd)
                    || !this.analysisBase.readShowUntil(telnet, readUntil, new TimeS(time)).contains(readUntil)) {
                return false;
            }
        }
        return true;
    }

}
