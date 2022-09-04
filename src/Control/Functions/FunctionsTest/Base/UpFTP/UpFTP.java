/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpFTP;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ManagerUI.UIStatus.UiStatus;
import Communicate.FtpClient.FtpClient;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionParameters;
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

    public UpFTP(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public UpFTP(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
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
        try {
            String FtpPath = craeteFtpPath();
            String localPath = craeteLocalPath();
            return upFile(localPath, FtpPath);
        } finally {
            this.baseFunc.disConnect(ftp);
        }
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
            addLog("Config", "Directory of local is null");
            return false;
        }
        if (ftpFile == null) {
            addLog("Config", "Directory of FTP is null");
        }
        File localFile = new File(local);
        if (!localFile.exists()) {
            addLog("Config", String.format("File \"%s\" not exists! ", local));
            return false;
        }
        addLog("Config", "Local: " + localFile.getPath());
        addLog("Config", "Ftp: " + ftpFile);
        if (ftp.uploadFile(localFile.getPath(), ftpFile)) {
            addLog("Up file to FTP done!");
            return true;
        }
        addLog("Up file to FTP faied!");
        return false;
    }

}
