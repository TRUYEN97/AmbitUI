/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeS;
import commandprompt.Communicate.Comport.ComPort;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class OpenShort extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;
    private int fixtureBaud;
    private String fixtureCom;
    private String dutCom;
    private int dutBaud;

    public OpenShort(String itemName) {
        super(itemName);
        this.functionBase = new FunctionBase(itemName);
        this.fixtureAction = new FixtureAction(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.fixtureAction.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    public void setFixtureComport(String port, int baud) {
        this.fixtureCom = port;
        this.fixtureBaud = baud;
    }

    public void setDutComport(String port, int baud) {
        this.dutCom = port;
        this.dutBaud = baud;
    }

    @Override
    protected boolean test() {
        if (fixtureCom == null) {
            fixtureCom = this.allConfig.getString("FixtureCom");
            fixtureBaud = this.allConfig.getInteger("FixtureBaudRate", 9600);
        }
        if (fixtureCom == null) {
            fixtureCom = this.allConfig.getString("DutCom");
            fixtureBaud = this.allConfig.getInteger("DutBaudRate", 9600);
        }
        ComPort fixture = this.functionBase.getComport(fixtureCom, fixtureBaud);
        ComPort dut = this.functionBase.getComport(dutCom, dutBaud);
        if (retry > 1 && !reConnectPortFixture(fixture)) {
            return false;
        }
        String keyWord = this.allConfig.getString("DutKey");
        if (!this.functionBase.sendCommand(dut, "\r\n") ) {
            return false;
        }
        int time = this.allConfig.getInteger("DutTime", 1);
        if(!this.analysisBase.isResponseContainKey(dut, keyWord, keyWord, new TimeS(time))){
            
        }
        return true;
    }

    public boolean reConnectPortFixture(ComPort fixture) {
        int time = this.allConfig.getInteger("FixtureTime", 1);
        List<String> portReconnect = this.allConfig.getListJsonArray("Reconnect");
        String fixtureKey = this.allConfig.getString("FixtureKey");
        return this.fixtureAction.sendCommand(fixture, portReconnect, fixtureKey, time);
    }
}
