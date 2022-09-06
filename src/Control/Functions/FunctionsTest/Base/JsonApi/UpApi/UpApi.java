/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.UpApi;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ManagerUI.UIStatus.UiStatus;
import Communicate.Cmd.Cmd;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpApi extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public UpApi(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public UpApi(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        String command = this.config.getString("Command");
        List<String> elementName = this.config.getListJsonArray("ElementName");
        String nameFile = this.fileBaseFunction.createNameFile(elementName,"");
        Cmd cmd = new Cmd();
        if (!this.functionBase.sendCommand(cmd, command + nameFile)) {
            return false;
        }
        String spec = config.getString("Spec");
        String response = cmd.readAll();
        addLog("Cmd", response);
        return response.trim().endsWith(spec);
    }

}
