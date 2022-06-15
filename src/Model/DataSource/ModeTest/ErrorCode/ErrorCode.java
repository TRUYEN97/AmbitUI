/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.ErrorCode;

import Model.DataSource.Tool.ReadFileSource;
import Model.Interface.ITypeRead;

/**
 *
 * @author Administrator
 */
public class ErrorCode extends ReadFileSource {

    public ErrorCode(ITypeRead typeRead) {
        super(typeRead);
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
