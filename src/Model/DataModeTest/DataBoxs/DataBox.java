/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class DataBox {

    private final MyLoger loger;
    private String resultTest;

    public DataBox(UiStatus uiStatus, String itemName) {
        this.loger = new MyLoger();
        String fileLogName = String.format("Log\\TestLog\\%s\\%s.txt", uiStatus.getName(), itemName);
        this.loger.begin(new File(fileLogName), true, true);
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

}
