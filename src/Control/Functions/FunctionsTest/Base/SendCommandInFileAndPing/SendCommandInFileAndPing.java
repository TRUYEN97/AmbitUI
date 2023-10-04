/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.SendCommandInFileAndPing;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.DutPing.DutPing;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SendCommandInFileAndPing extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;
    private final DutPing dutPing;

    public SendCommandInFileAndPing(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    public SendCommandInFileAndPing(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.functionBase = new FunctionBase(functionParameters, itemName);
        this.analysisBase = new AnalysisBase(functionParameters, itemName);
        this.dutPing = new DutPing(functionParameters, itemName);
    }

    @Override
    protected boolean test() {
        try {
            String filename = this.config.getString("fileName");
            addLog(LOG_KEYS.CONFIG, "file name: %s", filename);
            File file;
            if (filename == null || filename.isBlank() || !(file = new File(filename)).exists()) {
                addLog(LOG_KEYS.PC, "file \"%s\" not exist!", filename);
                return false;
            }
            String ip = this.functionBase.getIp();
            String readUntil = this.config.getString("ReadUntil");
            int time = this.config.getInteger("Time", 10);
            try ( Telnet telnet = this.functionBase.getTelnet(ip)) {
                if (telnet == null) {
                    return false;
                }
                List<String> commands = Files.readAllLines(file.toPath());
                for (String command : commands) {
                    if (!this.functionBase.sendCommand(telnet, command)) {
                        return false;
                    }
                    if (this.analysisBase.readShowUntil(telnet, readUntil, new TimeS(time)) == null) {
                        return false;
                    }
                }
                return this.dutPing.test();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

}
