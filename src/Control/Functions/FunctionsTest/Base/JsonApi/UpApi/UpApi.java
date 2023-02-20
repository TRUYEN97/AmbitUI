/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.UpApi;

import Communicate.Impl.Cmd.Cmd;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi.CreateJsonApi;
import Control.Functions.FunctionsTest.Base.TxtLog.CreateLog.CreateTxt;
import Control.Functions.FunctionsTest.Base.TxtLog.ZipLog.ZipFile;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpApi extends AbsFunction {

    private final FunctionBase functionBase;
    private final FileBaseFunction fileBaseFunction;
    private final CreateJsonApi jsonApi;
    private final CreateTxt createTxt;
    private final ZipFile zipFile;

    public UpApi(FunctionParameters parameters) {
        this(parameters, null);
    }

    public UpApi(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.jsonApi = new CreateJsonApi(parameters, item);
        this.createTxt = new CreateTxt(parameters, item);
        this.zipFile = new ZipFile(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        List<String> prefixElem = this.config.getJsonList("LocalPrefix");
        List<String> jsonElem = this.config.getJsonList("LocaljsonName");
        List<String> txtElem = this.config.getJsonList("LocalTxtName");
        String prefix = this.fileBaseFunction.createName(prefixElem);
        String jsonName = this.fileBaseFunction.createName(jsonElem);
        String jsonPath = String.format("%s/%s.json", prefix, jsonName);
        String txtPath = String.format("%s/%s.txt", prefix, this.fileBaseFunction.createName(txtElem));
        String zipPath = txtPath != null ? txtPath.replaceAll(".txt", ".zip") : null;
        Cmd cmd = new Cmd();
        String command = this.config.getString("Command");
        if (isCreateJsonOk(jsonPath)
                && isCreateTxtOk(txtPath)
                && isCreateZipOk(zipPath, txtPath)
                && this.functionBase.sendCommand(cmd, command + jsonName)) {
            String spec = config.getString("Spec");
            String response = cmd.readAll();
            addLog("Cmd", response);
            return response.trim().endsWith(spec);
        }
        return false;
    }

    private boolean isCreateZipOk(String zipPath, String txtPath) {
        try {
            this.zipFile.setZipPath(zipPath);
            this.zipFile.setTxtpath(txtPath);
            return this.zipFile.test();
        } finally {
            addLog("---------------------------------------------------------------");
        }
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
