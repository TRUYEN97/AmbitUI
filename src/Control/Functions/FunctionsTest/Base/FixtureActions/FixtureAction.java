/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.FixtureActions;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FixtureAction extends AbsFunction {

    private final FunctionBase base;
    private final AnalysisBase analysisBase;

    public FixtureAction(FunctionParameters parameters) {
        this(parameters, null);
    }

    public FixtureAction(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.base = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    public boolean test() {
        try ( ComPort comPort = getComport()) {
            if (comPort == null) {
                return false;
            }
            List<String> commands = this.config.getJsonList("FixtureCommands");
            List<String> keyWords = this.config.getJsonList("FixtureKeys");
            int time = this.config.getInteger("FixtureWait", 1);
            return sendCommand(comPort, commands, keyWords, time);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    public boolean sendCommand(ComPort comPort, List<String> commands, List<String> keyWords, int time) {
        addLog("Config", "keyWord: " + keyWords);
        addLog("Config", "Time: " + time);
        if (commands == null || commands.isEmpty() || keyWords == null || keyWords.isEmpty()) {
            addLog("ERROR", "Config failed");
            return false;
        }
        int keySide = keyWords.size();
        for (int index = 0; index < keySide; index++) {
            keyWords.set(index, getValueOfKey(keyWords.get(index)));
        }
        addLog("PC", String.format("True keys: %s", keyWords));
        try {
            for (String command : commands) {
                if (!this.base.sendCommand(comPort, command)) {
                    return false;
                }
                if (!this.analysisBase.isResponseContainKey(comPort, keyWords, new TimeS(time))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private String getValueOfKey(String keyWord) {
        return this.processData.getString(keyWord) == null ? keyWord
                : this.processData.getString(keyWord);
    }

    public ComPort getComport() {
        String com = this.config.getString("FixtureCom");
        int baud = this.config.getInteger("FixtureBaudRate", 9600);
        return this.base.getComport(com, baud);
    }

}
