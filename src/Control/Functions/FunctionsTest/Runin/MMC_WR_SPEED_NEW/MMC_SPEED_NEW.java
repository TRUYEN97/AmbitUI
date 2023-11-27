/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED_NEW;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_SPEED_NEW extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private String data;
    private String key;

    public MMC_SPEED_NEW(FunctionParameters parameters) {
        this(parameters, null);
    }

    public MMC_SPEED_NEW(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        if (data == null) {
            try {
                try (AbsCommunicate communicate = this.baseFunc.getTelnetOrComportConnector()) {
                    if (communicate == null) {
                        return false;
                    }
                    if (!this.baseFunc.sendCommand(communicate, this.config.getString("command"))) {
                        return false;
                    }
                    int time = this.config.getInteger("Time", 5);
                    String until = this.config.getString("ReadUntil");
                    data = this.analysisBase.readShowUntil(communicate, until, new TimeS(time));
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorLog.addError(this, e.getMessage());
                    return false;
                }
                addLog("Telnet", data);
            } catch (Exception ex) {
                ex.printStackTrace();
                addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
                return false;
            }
        }
        return checkResponse();
    }

    public void setData(String data, String key) {
        this.data = data;
        this.key = key;
    }

    private boolean checkResponse() {
        if (!checkConfig()) {
            return false;
        }
        List<Double> values = getSpeed();
        addLog("PC", "Values: %s", values);
        if(values == null || values.isEmpty()){
            return false;
        }
        double sum = 0;
        for (Double value : values) {
            sum += value;
        }
        double average = sum/values.size();
        String value = String.format("%.3f", average);
        addLog("PC", "The average value: %s", value);
        setResult(value);
        return true;
    }

    private boolean checkConfig() {
        if (key == null) {
            key = this.config.getString("KeyWord");
        }
        addLog("Config", "KeyWord: %s", key);
        return key != null;
    }

    private List<Double> getSpeed() {
        String[] blockData = data.split("\r\n");
        String line;
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < blockData.length; i++) {
            line = blockData[i].trim();
            if (line.contains(key)) {
                for (int j = i+1; j < blockData.length; j++) {
                    line = blockData[j].trim();
                    addLog("Data", line);
                    if(!line.contains("MB/s")){
                        return values;
                    }
                    values.add(this.analysisBase.string2Double(line.replaceAll("MB/s", "")));
                }
            }
        }
        return values;
    }

}
