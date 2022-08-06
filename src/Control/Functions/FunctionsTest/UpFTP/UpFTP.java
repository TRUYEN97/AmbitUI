/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.UpFTP;

import Control.Functions.AbsFunction;
import ftpclient.FtpClient;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class UpFTP extends AbsFunction {

    private FtpClient ftp;

    public UpFTP(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        if (!initFtp()) {
            addLog("Connect failed!!!");
        }
        addLog("Connect success.!");
        if (upFile(getFilePathFormSignal("FilePath"))) {
            addLog("Up file to FTP done!");
            return true;
        }
        addLog("Up file to FTP faied!");
        return false;
    }

    private boolean initFtp() {
        addLog("Connect to ftp!!");
        String user = funcConfig.getValue("FtpUser");
        addLog("User: " + user);
        String passWord = funcConfig.getValue("FtpPassword");
        addLog("PassWord: " + passWord);
        String host = funcConfig.getValue("FtpHost");
        addLog("Host: " + host);
        int port = Integer.valueOf(funcConfig.getValue("FtpPort"));
        addLog("Port: " + port);
        ftp = new FtpClient(host, port, user, passWord);
        return ftp.connect();
    }

    private String getFilePathFormSignal(String configKey) {
        String key = funcConfig.getValue(configKey);
        addLog("Get filePath in Signal with key: " + key);
        String filePath = uiData.getSignal(key).toString();
        addLog("filePath: " + filePath);
        return filePath;
    }

    private boolean upFile(String filePath) {
        File localFile = new File(filePath);
        if (!localFile.exists()) {
            addLog(String.format("File \"%s\" not exists! ", filePath));
            return false;
        }
        String dirFtp = funcConfig.getValue("dirFtp");
        if (dirFtp == null) {
            addLog("Directory of FTP is null with key: dirFtp");
        }
        String newFtpFile = String.format("%s/%s", dirFtp, localFile.getName());
        addLog("Ftp: " + newFtpFile);
        return ftp.uploadFile(localFile.getPath(), newFtpFile);
    }

}
