/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;

import Control.Functions.FunctionCover;
import MyLoger.MyLoger;
import java.io.File;

/**
 *
 * @author 21AK22
 */
public class ErrorLog {

    private static volatile ErrorLog instance;
    private final MyLoger loger;

    private ErrorLog() {
        this.loger = new MyLoger();
        String filePath = String.format("Log\\ErrorLog\\%s.txt",
                new TimeBase.TimeBase().getDate());
        this.loger.begin(new File(filePath), true);
    }

    public static ErrorLog getInstance() {
        ErrorLog ins = ErrorLog.instance;
        if (ins == null) {
            synchronized (ErrorLog.class) {
                ins = ErrorLog.instance;
                if (ins == null) {
                    ins = ErrorLog.instance = new ErrorLog();
                }
            }
        }
        return ins;
    }

    public void addError(String error) {
        this.loger.addLog(error);
    }

    public void addError(Object object, String error) {
        String mess = String.format("error in %s : %s", 
                object.getClass().getName(), error);
        this.loger.addLog(mess);
    }

}
