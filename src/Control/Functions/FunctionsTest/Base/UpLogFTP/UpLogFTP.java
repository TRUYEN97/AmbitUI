/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpLogFTP;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi.CreateJsonApi;
import Control.Functions.FunctionsTest.Base.TxtLog.CreateLog.CreateTxt;
import Control.Functions.FunctionsTest.Base.UpFTP.UpFTP;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpLogFTP extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;
    private final UpFTP upFTP;
    private final CreateJsonApi jsonApi;
    private final CreateTxt createTxt;

    public UpLogFTP(FunctionParameters parameters) {
        this(parameters, null);
    }

    public UpLogFTP(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
        this.upFTP = new UpFTP(parameters, item);
        this.jsonApi = new CreateJsonApi(parameters, item);
        this.createTxt = new CreateTxt(parameters, item);
    }

    @Override
    public boolean test() {
        try {
            List<String> localPrefix = this.config.getJsonList("LocalPrefix");
            List<String> ftpPrefix = this.config.getJsonList("FtpPrefix");
            List<String> localName = this.config.getJsonList("LocalName");
            List<String> localNameFail = this.config.getJsonList("LocalNameFail", localName);
            String ftpFolder = this.fileBaseFunction.createDir(ftpPrefix);
            String localFolder = this.fileBaseFunction.createName(localPrefix);
            String fileName = this.fileBaseFunction.createName(this.processData.isPass() ? localName : localNameFail);
            String localJsonPath = String.format("%s/json/%s.json", localFolder, fileName);
            String localTxtPath = String.format("%s/text/%s.txt", localFolder, fileName);
            String ftpJsonPath = String.format("%s/json/%s.json", ftpFolder, fileName);
            String ftpTxtPath = String.format("%s/text/%s.txt", ftpFolder, fileName);
            if (upJson(localJsonPath, ftpJsonPath) && upTxt(localTxtPath, ftpTxtPath)) {
                this.productData.put("ftppath", ftpTxtPath);
                setResult(ftpTxtPath);
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
            return false;
        }
    }

    private boolean upJson(String localJsonPath, String ftpJsonPath) {
        return isCreatejsonOK(localJsonPath) && isUpFTPOK(localJsonPath, ftpJsonPath);
    }

    private boolean upTxt(String localTxtPath, String ftpTxtPath) {
        return isCreateTxtOK(localTxtPath) && isUpFTPOK(localTxtPath, ftpTxtPath);
    }

    private boolean isCreatejsonOK(String localJsonPath) {
        this.jsonApi.setPath(localJsonPath);
        return this.jsonApi.test();
    }

    private boolean isCreateTxtOK(String localJsonPath) {
        this.createTxt.setPath(localJsonPath);
        return this.createTxt.test();
    }

    private boolean isUpFTPOK(String localPath, String ftpPath) {
        this.upFTP.setLocalPath(localPath);
        this.upFTP.setFtpPath(ftpPath);
        return this.upFTP.test();
    }

}
