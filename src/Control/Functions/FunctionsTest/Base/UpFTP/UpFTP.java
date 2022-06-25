/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpFTP;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.CreateFaJson.CreateFaJson;
import ftpclient.FtpClient;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public boolean test() {
        if (!initFtp()) {
            addLog("Connect failed!!!");
        }
        addLog("Connect success.!");
         try {
            Thread.sleep(20000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CreateFaJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (upFile(getFilePathFormSignal("FilePath"))) {
            addLog("Up file to FTP done!");
            return true;
        }
        addLog("Up file to FTP faied!");
        return false;
    }

    private boolean initFtp() {
        addLog("Connect to ftp!!");
        String user = this.allConfig.getString("FtpUser");
        addLog("User: " + user);
        String passWord = this.allConfig.getString("FtpPassword");
        addLog("PassWord: " + passWord);
        String host = this.allConfig.getString("FtpHost");
        addLog("Host: " + host);
        int port = Integer.valueOf(this.allConfig.getString("FtpPort"));
        addLog("Port: " + port);
        ftp = new FtpClient(host, port, user, passWord);
        return ftp.connect();
    }

    private String getFilePathFormSignal(String configKey) {
        String key = this.allConfig.getString(configKey);
        addLog("Get filePath in Signal with key: " + key);
        if (uiData.getSignal(key) == null) {
            addLog("Get filePath in Signal with key: " + key + " failed!");
            return null;
        }
        String filePath = uiData.getSignal(key).toString();
        addLog("filePath: " + filePath);
        return filePath;
    }

    private boolean upFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        File localFile = new File(filePath);
        if (!localFile.exists()) {
            addLog(String.format("File \"%s\" not exists! ", filePath));
            return false;
        }
        String dirFtp = this.allConfig.getString("dirFtp");
        if (dirFtp == null) {
            addLog("Directory of FTP is null with key: dirFtp");
        }
        String newFtpFile = String.format("%s/%s", dirFtp, localFile.getName());
        addLog("Ftp: " + newFtpFile);
        return ftp.uploadFile(localFile.getPath(), newFtpFile);
    }

}
