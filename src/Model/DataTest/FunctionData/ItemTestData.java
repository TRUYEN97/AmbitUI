/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import MyLoger.MyLoger;
import Time.TimeBase;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class ItemTestData {

    private static final String FAIL = "failed";
    private static final String PASS = "passed";
    private final FuncAllConfig allConfig;
    private final JSONObject data;
    private final JSONObject error;
    private final TimeS timeS;
    private final List<String> keys;
    private MyLoger loger;
    private boolean testing;

    public ItemTestData(FuncAllConfig allConfig) {
        this.allConfig = allConfig;
        this.keys = Arrays.asList(AllKeyWord.TEST_NAME,
                AllKeyWord.LOWER_LIMIT,
                AllKeyWord.UPPER_LIMIT,
                AllKeyWord.UNITS);
        this.data = new JSONObject();
        this.error = new JSONObject();
        this.timeS = new TimeS();
    }

    public JSONObject getData(List<String> keys) {
        if (keys == null) {
            return data;
        }
        JSONObject newData = new JSONObject();
        for (String key : keys) {
            var value = this.data.get(key);
            newData.put(key, value == null ? "" : value);
        }
        return newData;
    }

    public void start() {
        this.timeS.start(0);
        this.testing = true;
        isErrorCodeAvailable();
        for (String key : keys) {
            this.data.put(key, allConfig.getString(key));
        }
        this.data.put(AllKeyWord.START_TIME, new TimeBase().getSimpleDateTime());
    }

    private boolean isErrorCodeAvailable() {
        return this.allConfig.getLocalErrorCode(ErrorCodeElement.SIMPLE) != null;
    }

    public void put(String key, String value) {
        this.data.put(key, value);
    }

    public String getString(String key) {
        return data.getString(key);
    }

    public String getItemTestName() {
        return this.allConfig.getItemName();
    }

    public boolean isPass() {
        String stt = this.data.getString(AllKeyWord.STATUS);
        return (stt != null && stt.equals(PASS));
    }

    public void setPass(boolean isPass) {
        this.data.put(AllKeyWord.STATUS, isPass ? PASS : FAIL);
        if (getResultTest() == null) {
            this.data.put(AllKeyWord.TEST_VALUE, isPass ? PASS : FAIL);
        }
    }

    public void endThisTurn() {
        this.loger.addLog("****************************************************");
        addLimitData();
        this.loger.addLog(String.format("Test Value: \"%s\"", getResultTest()));
        this.loger.addLog("Item name: " + this.data.getString(AllKeyWord.TEST_NAME));
        this.loger.addLog("****************************************************");
    }

    private void addLimitData() {
        if (allConfig.getString(AllKeyWord.LIMIT_TYPE) == null) {
            return;
        }
        String limitType = allConfig.getString(AllKeyWord.LIMIT_TYPE);
        String uperLimit = allConfig.getString(AllKeyWord.UPPER_LIMIT);
        String lowerLimit = allConfig.getString(AllKeyWord.LOWER_LIMIT);
        this.loger.addLog(String.format("Limit type: \"%s\"", limitType));
        this.loger.addLog(String.format("Uper limit: \"%s\"", uperLimit));
        this.loger.addLog(String.format("Lowet limit: \"%s\"", lowerLimit));
    }

    public void clearError() {
        this.error.clear();
    }

    public void end() {
        logEnd();
        this.testing = false;
    }

    private void setErrorCode() {
        String errorCode = allConfig.getString(AllKeyWord.ERROR_CODE);
        this.error.put(AllKeyWord.ERROR_DES, this.allConfig.getItemName());
        if (errorCode != null && !errorCode.isBlank()) {
            this.error.put(AllKeyWord.ERROR_CODE, errorCode);
        } else {
            this.error.put(AllKeyWord.ERROR_CODE, "-1");
        }
    }

    private void setLocalErrorCode(String type) {
        JSONObject errorCode = this.allConfig.getLocalErrorCode(type);
        if (errorCode == null) {
            errorCode = this.allConfig.getLocalErrorCode(ErrorCodeElement.SIMPLE);
            if (errorCode == null) {
                return;
            }
        }
        this.error.putAll(errorCode);
    }
    
    public void setFail(String errorType) {
        this.setErrorCode();
        this.setLocalErrorCode(errorType);
        this.setPass(false);
    }

    public void setResult(String result) {
        this.data.put(AllKeyWord.TEST_VALUE, result);
    }

    public String getResultTest() {
        return this.data.getString(AllKeyWord.TEST_VALUE);
    }
    
    public String getStatusTest() {
        return this.data.getString(AllKeyWord.STATUS);
    }

    public boolean isTest() {
        return testing;
    }

    public double getRunTime() {
        if (isTest()) {
            return timeS.getTime();
        }
        return this.data.getDouble(AllKeyWord.CYCLE_TIME);
    }

    void setLoger(MyLoger loger) {
        this.loger = loger;
    }

    private void logEnd() {
        if (!isPass()) {
            this.data.putAll(this.error);
            String errorCode = this.data.getString(AllKeyWord.ERROR_CODE);
            String localErrorCode = this.data.getString(AllKeyWord.LOCAL_ERROR_CODE);
            String localErrorDes = this.data.getString(AllKeyWord.LOCAL_ERROR_DES);
            this.loger.addLog("Error code: " + errorCode);
            this.loger.addLog("Local error code: " + localErrorCode);
            this.loger.addLog("Local error des: " + localErrorDes);
        }
        this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", timeS.getTime()));
        this.data.put(AllKeyWord.FINISH_TIME, new TimeBase().getSimpleDateTime());
    }

}
