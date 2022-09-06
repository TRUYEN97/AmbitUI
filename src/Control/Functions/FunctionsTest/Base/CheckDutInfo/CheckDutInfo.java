/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckDutInfo;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Time.WaitTime.Class.TimeMs;
import Communicate.Telnet.Telnet;
import Model.DataTest.FunctionParameters;

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
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return check(ip);
    }
    
    private boolean check(String ip) {
        Telnet telnet = this.baseFunc.getTelnet(ip, 23);
        try {
            if (telnet == null || !this.baseFunc.sendCommand(telnet, config.getString("command"))) {
                return false;
            }
            String startkey = config.getString("Startkey");
            String endkey = config.getString("Endkey");
            String regex = config.getString("Regex");
            return checkValue(this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeMs(1000)));
        } finally {
            this.baseFunc.disConnect(telnet);
        }
        
    }
    
    private boolean checkValue(String value) {
        addLog("Value is: " + value);
        String spec = productData.getString("compareWith");
        addLog("Spec is: " + spec);
        setResult(value);
        return spec != null && value != null && spec.equals(value);
    }
    
}
