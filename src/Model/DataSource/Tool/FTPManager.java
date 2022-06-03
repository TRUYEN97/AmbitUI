/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Tool;

import Model.DataModeTest.ErrorLog;
import Model.DataSource.Setting.Setting;
import Model.Interface.IInit;
import ftpclient.FtpClient;

/**
 *
 * @author Administrator
 */
public class FTPManager implements IInit {

    private static volatile FTPManager instance;
    private int port;
    private String user;
    private String host;
    private String passWord;

    private FTPManager() {
        port = 21;
    }

    public static FTPManager getInstance() {
        FTPManager ins = instance;
        if (ins == null) {
            synchronized (FTPManager.class) {
                ins = FTPManager.instance;
                if (FTPManager.instance == null) {
                    FTPManager.instance = ins = new FTPManager();
                }
            }
        }
        return ins;
    }

    public FtpClient getNewClieant() {
        try {
            return new FtpClient(host, port, user, passWord);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean init() {
        Setting setting = Setting.getInstance();
        this.host = setting.getFtpHost();
        this.port = setting.getFtpPort();
        this.user = setting.getFtpUser();
        this.passWord = setting.getFtpPassWord();
        return getNewClieant() != null && getNewClieant().isConnect();
    }
}
