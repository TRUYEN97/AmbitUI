/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.AllKeyWord;
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

    private final String itemTestName;
    private final FuncAllConfig allConfig;
    private final JSONObject data;
    private final TimeS timeS;
    private final List<String> keys;
    private MyLoger loger;
    private boolean isPass;
    private boolean testing;

    public ItemTestData(FuncAllConfig allConfig) {
        this.itemTestName = allConfig.getItemName();
        this.allConfig = allConfig;
        this.keys = Arrays.asList(AllKeyWord.TEST_NAME,
                AllKeyWord.LOWER_LIMIT,
                AllKeyWord.UPPER_LIMIT,
                AllKeyWord.UNITS);
        this.data = new JSONObject();
        this.timeS = new TimeS();
        this.isPass = false;
    }

    public void start() {
        this.timeS.start(0);
        this.testing = true;
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
        return itemTestName;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean isPass) {
        this.isPass = isPass;
    }

    public void endThisTurn() {
        String limitType = allConfig.getString(AllKeyWord.LIMIT_TYPE);
        String uperLimit = allConfig.getString(AllKeyWord.UPPER_LIMIT);
        String lowerLimit = allConfig.getString(AllKeyWord.LOWER_LIMIT);
        this.loger.addLog(String.format("Limit type: \"%s\"", limitType));
        this.loger.addLog(String.format("Uper limit: \"%s\"", uperLimit));
        this.loger.addLog(String.format("Lowet limit: \"%s\"", lowerLimit));
        this.loger.addLog(String.format("Value: \"%s\"", getResultTest()));
    }

    public void end() {
        if (!isPass) {
            addErrorCode();
        }
        addResult();
        logEnd();
        this.testing = false;
    }

    private void addResult() {
        if (getResultTest() == null) {
            this.data.put(AllKeyWord.TEST_VALUE, isPass ? "PASS" : "FAIL");
        }
        this.data.put(AllKeyWord.STATUS, isPass ? "passed" : "failed");
        this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", timeS.getTime()));
        this.data.put(AllKeyWord.FINISH_TIME, new TimeBase().getSimpleDateTime());
    }

    private void addErrorCode() {
        String errorCode = allConfig.getString(AllKeyWord.ERROR_CODE);
        this.data.put(AllKeyWord.ERROR_DES, itemTestName);
        if (errorCode != null && !errorCode.isBlank()) {
            this.data.put(AllKeyWord.ERROR_CODE, errorCode);
        } else {
            this.data.put(AllKeyWord.ERROR_CODE, "-1");
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
        String error = this.data.getString(AllKeyWord.ERROR_CODE);
        if (error != null) {
            this.loger.addLog("ErrorCode: " + error);
        }
        String item = this.data.getString(AllKeyWord.TEST_NAME);
        this.loger.addLog("Item name: " + item);
    }

}
