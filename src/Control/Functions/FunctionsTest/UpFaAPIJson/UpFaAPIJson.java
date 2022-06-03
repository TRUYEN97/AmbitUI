/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.UpFaAPIJson;

import Control.Functions.AbsFunction;
import Model.DataModeTest.ErrorLog;
import Model.DataSource.Tool.FTPManager;
import View.subUI.FormDetail.TabFaApi.TabFaApi;
import com.alibaba.fastjson.JSONObject;
import ftpclient.FtpClient;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class UpFaAPIJson extends AbsFunction {

    private final FtpClient ftpClient;
    private TabFaApi faApi;
    public UpFaAPIJson(String itemName) {
        super(itemName);
        this.ftpClient = FTPManager.getInstance().getNewClieant();
    }

    @Override
    public boolean test() {
        init();
        if (waitData()) {
            return upAPI();
        }
        return false;
    }

    private boolean waitData() {
        addLog("Check tab FaAPI");
        if (this.uIData.getSignal(TabFaApi.MY_KEY) == null) {
            if (cancelThisTime()) {
                addLog("User has cancel this time!");
                return false;
            }
            addLog("Waiting for user config data!");
            waitForTabNotNull();
        }
        try {
            addLog("Get tab faAPi in Signal!");
            faApi = (TabFaApi) this.uIData.getSignal(TabFaApi.MY_KEY);
            if (!faApi.checkSelectData() && faApi.checkDataHasChange()) {
                JOptionPane.showMessageDialog(null, "Hãy xác nhận thông tin!");
            }
            addLog("Waiting for user config data!");
            waitUntilUserConfig(faApi);
            return true;
        } catch (HeadlessException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            addLog(e.getLocalizedMessage());
            return false;
        }
    }

    private void waitUntilUserConfig(TabFaApi faApi) {
        while (faApi.isTabEmpty() || faApi.checkDataHasChange()) {
            delay(200);
        }
    }

    private void waitForTabNotNull() {
        while (this.uIData.getSignal(TabFaApi.MY_KEY) == null) {
            delay(500);
        }
    }

    private boolean cancelThisTime() throws HeadlessException {
        int result = JOptionPane.showConfirmDialog(null,
                "Hãy xác nhận thông tin tại tab \"FA API\"",
                "Thông báo", JOptionPane.OK_CANCEL_OPTION);
        return result == JOptionPane.CANCEL_OPTION;
    }

    private void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.err.println(ex);
            ErrorLog.addError(this, ex.getLocalizedMessage());
        }
    }

    private boolean upAPI() {
        addLog("ad");
        if (this.ftpClient == null) {
            addLog("FtpClient is null!");
            return false;
        }
        JSONObject data = this.faApi.getData();
        return true;
    }

    private void init() {
    }

}
