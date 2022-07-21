/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpFTP;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import ftpclient.FtpClient;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpFTP extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;
    private FtpClient ftp;

    public UpFTP(String itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
        this.fileBaseFunction = new FileBaseFunction(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
        this.fileBaseFunction.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        String user = this.allConfig.getString("User");
        String passWord = this.allConfig.getString("Password");
        String host = this.allConfig.getString("Host");
        int port = this.allConfig.getInteger("Port", 21);
        ftp = this.baseFunc.initFtp(user, passWord, host, port);
        if (ftp == null) {
            return false;
        }
        String FtpPath = craeteFtpPath();
        String localPath = craeteLocalPath();
        return upFile(localPath, FtpPath);
    }

    private String craeteFtpPath() {
        List<String> elementName = this.allConfig.getListJsonArray("FtpName");
        List<String> elementPath = this.allConfig.getListJsonArray("FtpPath");
        String dir = this.fileBaseFunction.createDirPath(elementPath);
        String name = this.fileBaseFunction.createNameFile(elementName, allConfig.getString("FtpType"));
        return String.format("%s/%s", dir, name);
    }

    private String craeteLocalPath() {
        List<String> elementName = this.allConfig.getListJsonArray("LocalName");
        List<String> elementPath = this.allConfig.getListJsonArray("LocalPath");
        String dir = this.fileBaseFunction.createDirPath(elementPath);
        String name = this.fileBaseFunction.createNameFile(elementName, allConfig.getString("LocalType"));
        return String.format("%s/%s", dir, name);
    }

    private boolean upFile(String local, String ftpFile) {
        if (local == null) {
            addLog("Config","Directory of local is null");
            return false;
        }
        if (ftpFile == null) {
            addLog("Config","Directory of FTP is null");
        }
        File localFile = new File(local);
        if (!localFile.exists()) {
            addLog("Config",String.format("File \"%s\" not exists! ", local));
            return false;
        }
        addLog("Config","Local: " + localFile.getPath());
        addLog("Config","Ftp: " + ftpFile);
        if (ftp.uploadFile(localFile.getPath(), ftpFile)) {
            addLog("Up file to FTP done!");
            return true;
        }
        addLog("Up file to FTP faied!");
        return false;
    }

}
