/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.UpFaAPIJson;

import Control.Functions.AbsFunction;
import Model.DataModeTest.ErrorLog;
import View.subUI.FormDetail.TabFaApi.TabFaApi;
import ftpclient.FtpClient;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class UpFaAPIJson extends AbsFunction {

    private FtpClient ftpClient;
    public UpFaAPIJson(String itemName) {
        super(itemName);
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
            addLog("Wait for user config data!");
            waitForTabNotNull();
        }
        try {
            addLog("Get tab faAPi in Signal!");
            TabFaApi faApi = (TabFaApi) this.uIData.getSignal(TabFaApi.MY_KEY);
            if (!faApi.checkSelectData() && faApi.checkDataHasChange()) {
                JOptionPane.showMessageDialog(null, "Hãy xác nhận thông tin!");
            }
            addLog("Wait for user config data!");
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
        if (result == JOptionPane.CANCEL_OPTION) {
            return true;
        }
        return false;
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
        
    }

    private void init() {
    }

}
