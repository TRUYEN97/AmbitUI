/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.UpApi;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import commandprompt.Communicate.Cmd.Cmd;

/**
 *
 * @author Administrator
 */
public class UpApi extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public UpApi(String itemName) {
        super(itemName);
        this.functionBase = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
        this.fileBaseFunction = new FileBaseFunction(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.functionBase.setResources(uiStatus, functionData);
        this.analysisBase.setResources(uiStatus, functionData);
        this.fileBaseFunction.setResources(uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String command = this.allConfig.getString("Command");
        String nameFile = this.fileBaseFunction.createNameFile(this.allConfig.getListJsonArray("ElementName"), "");
        Cmd cmd = new Cmd();
        if (!this.functionBase.sendCommand(cmd, command + nameFile)) {
            return false;
        }
        String spec = allConfig.getString("Spec");
        String response = cmd.readAll();
        addLog("Cmd", response);
        return response.trim().endsWith(spec);
    }

}
