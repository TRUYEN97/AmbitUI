/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.ErrorCode;

import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;

/**
 *
 * @author Administrator
 */
public class ErrorCodeSource extends AbsJsonSource<ErrorCodeElement> {

    private static volatile ErrorCodeSource instaince;

    public ErrorCodeSource() {
        super();
    }

    public static ErrorCodeSource getInstance() {
        ErrorCodeSource ins = ErrorCodeSource.instaince;
        if (ins == null) {
            synchronized (ErrorCodeSource.class) {
                ins = ErrorCodeSource.instaince;
                if (ins == null) {
                    ErrorCodeSource.instaince = ins = new ErrorCodeSource();
                }
            }
        }
        return ins;
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        ErrorCodeElement info;
        for (String key : wareHouse.toJson().keySet()) {
            info = new ErrorCodeElement(null, null, wareHouse.getJson(key));
            put(key, info);
        }
        return !this.elements.isEmpty();
    }

}
