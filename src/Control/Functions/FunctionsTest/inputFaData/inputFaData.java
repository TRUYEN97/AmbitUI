/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.InputFaData;

import Control.Functions.AbsFunction;
import Model.DataTest.ErrorLog;
import View.subUI.FormDetail.TabFaApi.TabFaApi;
import com.alibaba.fastjson.JSONObject;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class InputFaData extends AbsFunction {

    private TabFaApi faApi;

    public InputFaData(String itemName) {
        super(itemName);
    }

    @Override
    public boolean test() {
        if (!waitData()) {
            return false;
        }
        try {
            addLog("Get tab faAPi in Signal!");
            faApi = (TabFaApi) this.uiData.getSignal(TabFaApi.MY_KEY);
            if (!faApi.checkSelectData() && faApi.checkDataHasChange()) {
                JOptionPane.showMessageDialog(null, "Hãy xác nhận thông tin!");
            }
            addLog("Waiting for user config data!");
            waitUntilUserConfig(faApi);
            addLog("Waiting for user config data done!");
            return addDataToSignal();
        } catch (HeadlessException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            addLog(e.getLocalizedMessage());
            return false;
        }
    }

    private boolean addDataToSignal() {
        try {
            addLog("Add select json data to signal!");
            JSONObject data = faApi.getData();
            String keyWord = funcConfig.getValue("KEY_WORD");
            addLog(data.toJSONString());
            this.uiData.putToSignal(keyWord, faApi.getData());
            addLog("keyword: " + keyWord);
            addLog("Add select json data to signal ok!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean waitData() {
        addLog("Check tab FaAPI");
        if (this.uiData.getSignal(TabFaApi.MY_KEY) == null) {
            if (cancelThisTime()) {
                addLog("User has cancel this time!");
                return false;
            }
            addLog("Waiting for user config data!");
            waitForTabNotNull();
        }
        return true;
    }

    private void waitUntilUserConfig(TabFaApi faApi) {
        while (faApi.isTabEmpty() || faApi.checkDataHasChange()) {
            delay(200);
        }
    }

    private void waitForTabNotNull() {
        while (this.uiData.getSignal(TabFaApi.MY_KEY) == null) {
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

}
