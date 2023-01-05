/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_BadBlock;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_BadBlock extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    

    public MMC_BadBlock(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public MMC_BadBlock(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return check(ip);
    }

    private boolean check(String ip) {
        try (Telnet telnet = this.baseFunc.getTelnet(ip, 23)){
            String startkey = config.getString("Startkey");
            String endkey = config.getString("Endkey");
            List<String> commands = this.config.getListJsonArray("command");
            if (commands == null || commands.isEmpty()) {
                addLog("ERROR", "command in config is null or empty!");
                return false;
            }
            return runCommand(telnet, commands, startkey, endkey);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean runCommand(Telnet telnet, List<String> commands, String startkey, String endkey) {
        Integer sunBadblock = 0;
        for (String subCommand : commands) {
            if (!this.baseFunc.sendCommand(telnet, subCommand)) {
                return false;
            }
            String result = this.analysisBase.getValue(telnet, startkey, endkey);
            if (!this.analysisBase.isNumber(result)) {
                return false;
            }
            sunBadblock += this.analysisBase.string2Integer(result);
            addLog("PC", "Sum of bad blocks: " + sunBadblock);
        }
        setResult(sunBadblock.toString());
        return true;
    }

}
