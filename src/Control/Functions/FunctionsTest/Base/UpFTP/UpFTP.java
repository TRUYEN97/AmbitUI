/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpFTP;

import Communicate.Impl.FtpClient.FtpClient;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class UpFTP extends AbsFunction {

    private final FunctionBase baseFunc;
    private final FileBaseFunction fileBaseFunction;
    private String ftpPath;
    private String localPath;

    public UpFTP(FunctionParameters parameters) {
        this(parameters, null);
    }

    public UpFTP(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public boolean test() {
        try {
            String user = this.config.getString("User");
            String passWord = this.config.getString("Password");
            String host = this.config.getString("Host");
            int port = this.config.getInteger("Port", 21);
            FtpClient ftp = this.baseFunc.initFtp(user, passWord, host, port);
            if (ftp == null) {
                return false;
            }
            ftpPath = ftpPath == null ? createFtpPath() : ftpPath;
            localPath = localPath == null ? createLocalPath() : localPath;
            return upFile(ftp, localPath, ftpPath);
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
            return false;
        }
    }

    private String createFtpPath() {
        String fileName = this.processData.isPass() ? "FtpName" : "FtpNameFail";
        return this.fileBaseFunction.createStringPath("FtpPrefix", fileName, "FtpSuffix");
    }

    private String createLocalPath() {
        return this.fileBaseFunction.createDefaultStringPath(this.processData.isPass());
    }

    private boolean upFile(FtpClient ftp, String local, String ftpFile) {
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
            addLog("PC", "Up file to FTP done!");
            addLog("________________________________________________________");
            return true;
        }
        addLog("PC", "Up file to FTP faied!");
        addLog("________________________________________________________");
        return false;
    }

}
