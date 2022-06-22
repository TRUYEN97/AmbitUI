/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataTest.FuncAllConfig;
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

    private final FuncAllConfig allConfig;
    private final JSONObject data;
    private final JSONObject error;
    private final TimeS timeS;
    private final List<String> keys;
    private MyLoger loger;
    private boolean isPass;
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
        this.isPass = false;
    }

    public void start() {
        this.timeS.start(0);
        this.testing = true;
        isErrorCodeAvailable();
        this.data.put(AllKeyWord.START_TIME, new TimeBase().getSimpleDateTime());
        for (String key : keys) {
            this.data.put(key, allConfig.getString(key));
        }
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
        return isPass;
    }

    public void setPass(boolean isPass) {
        this.isPass = isPass;
    }

    public void endThisTurn() {
        this.loger.addLog("****************************************************");
        addLimitData();
        if (getResultTest() == null) {
            this.data.put(AllKeyWord.TEST_VALUE, isPass ? "PASS" : "FAIL");
        }
        this.loger.addLog(String.format("Test Value: \"%s\"", getResultTest()));
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

    public void setErrorCode() {
        String errorCode = allConfig.getString(AllKeyWord.ERROR_CODE);
        this.error.put(AllKeyWord.ERROR_DES, this.allConfig.getItemName());
        if (errorCode != null && !errorCode.isBlank()) {
            this.error.put(AllKeyWord.ERROR_CODE, errorCode);
        } else {
            this.error.put(AllKeyWord.ERROR_CODE, "-1");
        }
    }

    public void setLocalErrorCode(String type) {
        JSONObject errorCode = this.allConfig.getLocalErrorCode(type);
        if (errorCode == null) {
            errorCode = this.allConfig.getLocalErrorCode(ErrorCodeElement.SIMPLE);
            if (errorCode == null) {
                return;
            }
        }
        for (String key : errorCode.keySet()) {
            this.error.put(key, errorCode.getString(key));
        }
    }

    public void setResult(String result) {
        this.data.put(AllKeyWord.TEST_VALUE, result);
    }

    public String getResultTest() {
        return this.data.getString(AllKeyWord.TEST_VALUE);
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
        if (!isPass) {
            String errorCode = this.data.getString(AllKeyWord.ERROR_CODE);
            String localErrorCode = this.data.getString(AllKeyWord.LOCAL_ERROR_CODE);
            String localErrorDes = this.data.getString(AllKeyWord.LOCAL_ERROR_DES);
            this.loger.addLog("Error code: " + errorCode);
            this.loger.addLog("Local error code: " + localErrorCode);
            this.loger.addLog("Local error des: " + localErrorDes);
        }
        this.loger.addLog("Item name: " + this.data.getString(AllKeyWord.TEST_NAME));
        this.loger.addLog("=====================================================");
        this.data.putAll(this.error);
        this.data.put(AllKeyWord.STATUS, isPass ? "passed" : "failed");
        this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", timeS.getTime()));
        this.data.put(AllKeyWord.FINISH_TIME, new TimeBase().getSimpleDateTime());
    }

    public void clearErrorCode() {
        this.error.clear();
    }

    private boolean isErrorCodeAvailable() {
        return this.allConfig.getLocalErrorCode(ErrorCodeElement.SIMPLE) != null;
    }

}
