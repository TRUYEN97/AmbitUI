/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutTelnet;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;

/**
 *
 * @author Administrator
 */
public class DutTelnet extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public DutTelnet(FunctionParameters parameters) {
        this(parameters, null);
    }

    public DutTelnet(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.functionBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        try ( Telnet telnet = this.functionBase.getTelnet(ip, 23)) {
            return telnet != null;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }
}
