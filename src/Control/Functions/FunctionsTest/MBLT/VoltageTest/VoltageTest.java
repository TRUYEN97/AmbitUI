/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;
import Model.DataTest.FunctionParameters;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class VoltageTest extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final AnalysisBase analysisBase;
    private final Map<String, String> voltageVals;

    public VoltageTest(FunctionParameters parameters) {
        this(parameters, null);
    }

    public VoltageTest(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fixtureAction = new FixtureAction(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.voltageVals = new HashMap<>();
    }

    @Override
    protected boolean test() {
        List<String> commands = this.config.getJsonList("Commands");
        List<String> readUntils = this.config.getJsonList("readUntil");
        String volPointPath = this.config.getString("filePath");
        addLog("Config", "Command: " + readUntils);
        addLog("Config", "read until: " + commands);
        addLog("Config", "file path: " + volPointPath);
        try ( ComPort fixture = this.fixtureAction.getComport()) {
            for (String command : commands) {
                if (fixture == null || !fixture.sendCommand(command)) {
                    return false;
                }
                String inline;
                while ((inline = fixture.readLine(new TimeS(1))) != null) {
                    addLog("PC", "-------------------------------------");
                    inline = inline.trim();
                    addLog("Comport", inline);
                    if (!inline.startsWith("TP") || !inline.contains("=")) {
                        continue;
                    }
                    if (inline.contains(",")) {
                        inline = inline.replaceAll(",", ".");
                    }
                    String[] slips = inline.split("=|:|;");
                    addLog(LOG_KEYS.PC, "Value: %s - %s",slips[0], slips[1]);
                    this.voltageVals.put(slips[0], slips[1]);
                    if (this.analysisBase.contains(inline, readUntils)) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
        try {
            Map<String, String> tpItemNames =new HashMap<>();
            List<String> stringPoints = Files.readAllLines(Path.of(volPointPath));
            for (String stringPoint : stringPoints) {
                String[] elems = stringPoint.split(",");
                if (elems.length < 2 || elems[0].isBlank() || elems[1].isBlank()) {
                    continue;
                }
                tpItemNames.put(elems[0], elems[1]);
            }
            for (String tp : this.voltageVals.keySet()) {
                if (!tpItemNames.containsKey(tp)) {
                    addLog(LOG_KEYS.PC, "no have %s in point list", tp);
                    continue;
                }
                addLog(LOG_KEYS.CONFIG, "point convert to item name: %s -> %s",
                        tp, tpItemNames.get(tp));
                VoltagePonit voltagePonit = new VoltagePonit(functionParameters, 
                        createChildItemName(tpItemNames.get(tp)),
                        this.voltageVals.get(tp));
                voltagePonit.runTest();
                if( !voltagePonit.isPass()){
                    return false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
            return false;
        }
        return true;
    }

}
