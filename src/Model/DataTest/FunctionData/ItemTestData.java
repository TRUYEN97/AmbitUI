/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataSource.Tool.IgetTime;
import MyLoger.MyLoger;
import Time.TimeBase;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class ItemTestData {

    public static final String FAIL = "failed";
    public static final String PASS = "passed";
    private final FuncAllConfig allConfig;
    private final JSONObject data;
    private final JSONObject error;
    private final IgetTime timer;
    private double startTime;
    private double testTime;
    private final List<String> keys;
    private final MyLoger loger;
    private boolean testing;

    public ItemTestData(FuncAllConfig allConfig, FunctionData functionData, IgetTime timer) {
         this.allConfig = allConfig;
        this.startTime = 0;
        this.keys = Arrays.asList(AllKeyWord.TEST_NAME,
                AllKeyWord.LOWER_LIMIT,
                AllKeyWord.UPPER_LIMIT,
                AllKeyWord.UNITS);
        this.data = new JSONObject();
        this.error = new JSONObject();
        this.loger = functionData.getLoger();
        this.timer = timer;
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
        this.startTime = this.timer.getRuntime();
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

    public String getItemName() {
        return this.allConfig.getItemName();
    }

    public boolean isPass() {
        String stt = this.data.getString(AllKeyWord.STATUS);
        return (stt != null && stt.equals(PASS));
    }

    private void setTestValue(boolean isPass) {
        this.data.put(AllKeyWord.STATUS, isPass ? PASS : FAIL);
        if (getResultTest() == null) {
            this.data.put(AllKeyWord.TEST_VALUE, isPass ? PASS : FAIL);
        }
    }

    public void end() {
        if (!isPass()) {
            writeError();
        }
        this.loger.addLog("****************************************************");
        this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", testTime = getRunTime()));
        this.data.put(AllKeyWord.FINISH_TIME, new TimeBase().getSimpleDateTime());
        this.testing = false;
    }

    private void setErrorCode() {
        String errorCode = allConfig.getString(AllKeyWord.ERROR_CODE);
        this.error.put(AllKeyWord.ERROR_DES, this.allConfig.getString(AllKeyWord.TEST_NAME));
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

    public void setPass() {
        this.error.clear();
        setTestValue(true);
    }

    public void setFail(String errorType) {
        this.setErrorCode();
        this.setLocalErrorCode(errorType);
        this.setTestValue(false);
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
            return timer.getRuntime()- this.startTime;
        }
        return testTime;
    }

    public void endTurn() {
        this.loger.addLog("****************************************************");
        this.loger.addLog(String.format("Item name = %s", this.data.getString(AllKeyWord.TEST_NAME)));
        addLimitData();
        this.loger.addLog(String.format("Value = %s", getResultTest()));
        this.loger.addLog(String.format("Test status = %s", getStatusTest()));
        this.loger.addLog("-----------------------------------------------------");
    }

    private void addLimitData() {
        if (allConfig.getString(AllKeyWord.LIMIT_TYPE) == null) {
            return;
        }
        String limitType = allConfig.getString(AllKeyWord.LIMIT_TYPE);
        String uperLimit = allConfig.getString(AllKeyWord.UPPER_LIMIT);
        String lowerLimit = allConfig.getString(AllKeyWord.LOWER_LIMIT);
        this.loger.addLog(String.format("Limit type = %s", limitType));
        this.loger.addLog(String.format("Uper limit = %s", uperLimit));
        this.loger.addLog(String.format("Lowet limit = %s", lowerLimit));
    }

    private void writeError() {
        this.data.putAll(this.error);
        String errorCode = this.data.getString(AllKeyWord.ERROR_CODE);
        String errorDes = this.data.getString(AllKeyWord.ERROR_DES);
        String localErrorCode = this.data.getString(AllKeyWord.LOCAL_ERROR_CODE);
        String localErrorDes = this.data.getString(AllKeyWord.LOCAL_ERROR_DES);
        this.loger.addLog(String.format("Error code = %s", errorCode));
        this.loger.addLog(String.format("Error des = %s", errorDes));
        this.loger.addLog(String.format("Local error code = %s", localErrorCode));
        this.loger.addLog(String.format("Local error des = %s", localErrorDes));
    }

}
