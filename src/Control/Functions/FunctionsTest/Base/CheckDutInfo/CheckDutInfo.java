/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckDutInfo;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;

/**
 *
 * @author Administrator
 */
public class CheckDutInfo extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public CheckDutInfo(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CheckDutInfo(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try ( AbsCommunicate communicate = this.baseFunc.getTelnetOrComportConnector()) {
            if (communicate == null || !this.baseFunc.sendCommand(communicate, config.getString("command"))) {
                return false;
            }
            String startkey = config.getString("Startkey");
            String endkey = config.getString("Endkey");
            String regex = config.getString("Regex");
            String readUntil = config.getString("ReadUntil");
            int time = config.getInteger("Time", 10);
            return checkValue(this.analysisBase.getValue(communicate, 
                    startkey, endkey, regex, new TimeS(time), readUntil));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean checkValue(String value) {
        addLog("PC", "Value is: " + value);
        String key = this.config.getString("compare");
        addLog("CONFIG", "compare key: " + key);
        String spec = productData.getString(key);
        addLog("PC", "Spec is: " + spec);
        setResult(value);
        return spec != null && value != null && spec.equalsIgnoreCase(value);
    }

}
