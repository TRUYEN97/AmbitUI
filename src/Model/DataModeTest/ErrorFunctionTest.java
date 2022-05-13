/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;


/**
 *
 * @author 21AK22
 */
public class ErrorFunctionTest {
    private final String errorCode;
    private final String errorDes;
    private final String CusErrorCode;
    private final String CusErrorDes;

    public ErrorFunctionTest(String errorCode, String errorDes, String CusErrorCode, String CusErrorDes) {
        this.errorCode = errorCode;
        this.errorDes = errorDes;
        this.CusErrorCode = CusErrorCode;
        this.CusErrorDes = CusErrorDes;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public String getCusErrorCode() {
        return CusErrorCode;
    }

    public String getCusErrorDes() {
        return CusErrorDes;
    }

    
    
}
