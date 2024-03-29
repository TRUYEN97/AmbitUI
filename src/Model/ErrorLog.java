/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import MyLoger.MyLoger;
import Time.TimeBase;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author 21AK22
 */
public class ErrorLog {

    private static final MyLoger loger = new MyLoger();

    static {
        String filePath = String.format("Log\\ErrorLog\\%s.txt",
                new TimeBase(TimeBase.UTC).getDate());
        ErrorLog.loger.setFile(new File(filePath));
        ErrorLog.loger.setSaveMemory(true);
    }

    public static void addError(String error) {
        try {
            ErrorLog.loger.addLog(error + "\r\n//////////////////////////////////////");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void addError(Object object, String error) {
        String mess = String.format("error in %s : %s",
                object.getClass().getName(), error);
        addError(mess);
    }

}
