/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.DataModeTest.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DataBox {

    private final MyLoger loger;
    private final String itemName;
    private String resultTest;

    public DataBox(UiStatus uiStatus, String itemName) {
        this.loger = new MyLoger();
        this.itemName = itemName;
        String fileLogName = String.format("Log\\TestLog\\%s\\%s.txt", uiStatus.getName(), itemName);
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + itemName;
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError( mess);
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            this.resultTest = new String();
            return;
        }
        this.resultTest = resultTest;
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }
    
    public String getResultTest() {
        return resultTest;
    }

    public void remove() {
        this.loger.close();
    }
}
