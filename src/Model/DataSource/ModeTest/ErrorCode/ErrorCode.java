/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.ErrorCode;

import Control.FileType.FileErrorMode;
import Model.DataSource.Tool.ReadFileSource;

/**
 *
 * @author Administrator
 */
public class ErrorCode extends ReadFileSource {

    private static volatile ErrorCode errorCode;

    private ErrorCode() {
        super(new FileErrorMode());
    }

    public static ErrorCode getInstance() {
        ErrorCode ins = ErrorCode.errorCode;
        if (ins == null) {
            synchronized (ErrorCode.class) {
                ins = ErrorCode.errorCode;
                if (ins == null) {
                    ErrorCode.errorCode = ins = new ErrorCode();
                }
            }
        }
        return ins;
    }

    public String getErrorCode(String errorDes) {
        return this.data.getString(errorDes);
    }

    public String getErrorDes(String errorCode) {
        for (String key : this.data.toJson().keySet()) {
            if (this.data.getString(key).equals(errorCode)) {
                return key;
            }
        }
        return null;
    }
}
