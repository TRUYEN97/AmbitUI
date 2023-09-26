/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.UploadLog;

import Communicate.Impl.Cmd.Cmd;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi.CreateJsonApi;
import Control.Functions.FunctionsTest.Base.TxtLog.CreateLog.CreateTxt;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UploadLog extends AbsFunction{

    private final FunctionBase functionBase;
    private final FileBaseFunction fileBaseFunction;
    private final CreateJsonApi jsonApi;
    private final CreateTxt createTxt;
    
    public UploadLog(FunctionParameters parameter) {
        this(parameter, null);
    }

    public UploadLog(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.jsonApi = new CreateJsonApi(parameters, item);
        this.createTxt = new CreateTxt(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        JSONObject bonus = (JSONObject) this.config.getObject("bonus");
        for (String key : bonus.keySet()) {
            this.productData.put(key, bonus.getString(key));
        }
        List<String> prefixElem = this.config.getJsonList("LocalPrefix");
        List<String> jsonElem = this.config.getJsonList("LocaljsonName");
        List<String> txtElem = this.config.getJsonList("LocalTxtName");
        String prefix = this.fileBaseFunction.createName(prefixElem);
        String jsonName = String.format("%s.json", this.fileBaseFunction.createName(jsonElem));
        String txtName = String.format("%s.txt",this.fileBaseFunction.createName(txtElem));
        String jsonPath = String.format("%s/%s", prefix, jsonName);
        String txtPath = String.format("%s/%s", prefix, txtName);
        Cmd cmd = new Cmd();
        String command = this.config.getString("Command");
        if (isCreateJsonOk(jsonPath)
                && isCreateTxtOk(txtPath)
                && this.functionBase.sendCommand(cmd, 
                        String.format("%s \"log/%s\" \"log/%s\"", 
                                command, jsonName, txtName))) {
            String spec = config.getString("Spec");
            int time = this.config.getInteger("Time", 10);
            addLog(LOG_KEYS.PC, "Waiting for API reponse about %s S", time);
            String response = cmd.readAll(new TimeS(time));
            addLog("Cmd", response);
            return response.trim().endsWith(spec);
        }
        return false;
    }

    private boolean isCreateTxtOk(String txtPath) {
        try {
            this.createTxt.setPath(txtPath);
            return this.createTxt.test();
        } finally {
            addLog("---------------------------------------------------------------");
        }
    }

    private boolean isCreateJsonOk(String jsonPath) {
        try {
            this.jsonApi.setPath(jsonPath);
            return this.jsonApi.test();
        } finally {
            addLog("---------------------------------------------------------------");
        }
    }
    
}
