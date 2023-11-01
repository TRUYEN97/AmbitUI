/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataSource.Tool.IgetTime;
import Model.ErrorLog;
import MyLoger.MyLoger;
import Time.TimeBase;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class ItemTestData {

    public static enum TYPE {
        INIT, TEST, END, FINAL
    };
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
    private int status;
    private final TYPE itemType;

    public ItemTestData(FuncAllConfig allConfig, MyLoger loger, IgetTime timer, TYPE itemType) {
        this.allConfig = allConfig;
        this.startTime = 0;
        this.keys = Arrays.asList(AllKeyWord.CONFIG.TEST_NAME,
                AllKeyWord.CONFIG.LOWER_LIMIT,
                AllKeyWord.CONFIG.UPPER_LIMIT,
                AllKeyWord.CONFIG.UNITS);
        this.data = new JSONObject();
        this.error = new JSONObject();
        this.loger = loger;
        this.timer = timer;
        this.itemType = itemType;
    }

    public TYPE getItemType() {
        return itemType;
    }

    public JSONObject getData(List<String> keys, boolean useLimitErrorcode) {
        if (keys == null) {
            return data;
        }
        JSONObject newData = new JSONObject();
        for (String key : keys) {
            Object value;
            if (!useLimitErrorcode && key.equalsIgnoreCase(AllKeyWord.CONFIG.ERROR_CODE)) {
                value = this.data.get(AllKeyWord.SFIS.ERRORCODE);
            } else {
                value = this.data.get(key);
            }
            newData.put(key, value == null ? "" : value);
        }
        return newData;
    }

    public JSONObject getData(List<String> keys) {
        return getData(keys, true);
    }

    public void start() {
        this.startTime = this.timer.getRuntime();
        this.error.clear();
        this.status = 1;
        for (String key : keys) {
            this.data.put(key, allConfig.getString(key));
        }
        this.data.put(AllKeyWord.START_TIME, new TimeBase(TimeBase.UTC).getSimpleDateTime());
    }

    public String getString(String key) {
        return data.getString(key);
    }

    public String getItemName() {
        return this.allConfig.getItemName();
    }

    public boolean isPass() {
        String stt = this.data.getString(AllKeyWord.SFIS.STATUS);
        return (stt != null && stt.equals(PASS));
    }

    private void setTestValue(boolean isPass) {
        this.data.put(AllKeyWord.SFIS.STATUS, isPass ? PASS : FAIL);
        String result = getResultTest();
        if (result == null || (isPass && result.equalsIgnoreCase(FAIL))) {
            this.data.put(AllKeyWord.TEST_VALUE, isPass ? PASS : FAIL);
        }
    }

    public void end() {
        if (isPass()) {
            this.data.put(AllKeyWord.CONFIG.ERROR_CODE, "");
            this.data.put(AllKeyWord.CONFIG.ERROR_DES, "");
            this.data.put(AllKeyWord.SFIS.ERRORCODE, "");
            this.data.put(AllKeyWord.SFIS.ERRORDES, "");
        } else {
            this.data.putAll(this.error);
            this.addLog(String.format("Error code = %s", getLimitsErrorCode()));
            this.addLog(String.format("Error desc = %s", getErrorDes()));
            this.addLog(String.format("Local error code = %s", getLocalErrorCode()));
            this.addLog(String.format("Local error desc = %s", getLocalErrorDes()));
        }
        this.addLog("****************************************************");
        this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", testTime = getRunTime()));
        this.data.put(AllKeyWord.FINISH_TIME, new TimeBase(TimeBase.UTC).getSimpleDateTime());
        this.status = 2;
    }

    private void addLog(String str) {
        try {
            this.loger.addLog(str);
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getLocalizedMessage());
        }
    }

    private void setErrorCode() {
        String errorCode = allConfig.getString(AllKeyWord.CONFIG.ERROR_CODE);
        this.error.put(AllKeyWord.CONFIG.ERROR_DES, this.allConfig.getString(AllKeyWord.CONFIG.TEST_NAME));
        if (errorCode != null && !errorCode.isBlank()) {
            this.error.put(AllKeyWord.CONFIG.ERROR_CODE, errorCode);
        } else {
            this.error.put(AllKeyWord.CONFIG.ERROR_CODE, "-1");
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
        error.clear();
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
        return this.data.getString(AllKeyWord.SFIS.STATUS);
    }

    public boolean isTest() {
        return status == 1;
    }

    public boolean isWait() {
        return status == 0;
    }

    public double getRunTime() {
        if (isTest()) {
            return timer.getRuntime() - this.startTime;
        }
        return testTime;
    }

    public void endTurn() {
        this.addLog("****************************************************");
        this.addLog(String.format("Item name = %s", this.data.getString(AllKeyWord.CONFIG.TEST_NAME)));
        if (allConfig.getString(AllKeyWord.CONFIG.LIMIT_TYPE) != null) {
            addLimitData();
        }
        this.addLog(String.format("Value = %s", getResultTest()));
        if (!this.allConfig.isCheckWithSpec() && this.allConfig.isSpecAvailable()) {
            this.addLog("Skip check spec");
        }
        this.addLog(String.format("Test status = %s", getStatusTest()));
        this.addLog("-----------------------------------------------------");
    }

    private void addLimitData() {
        String limitType = allConfig.getString(AllKeyWord.CONFIG.LIMIT_TYPE);
        String uperLimit = allConfig.getString(AllKeyWord.CONFIG.UPPER_LIMIT);
        String lowerLimit = allConfig.getString(AllKeyWord.CONFIG.LOWER_LIMIT);
        this.addLog(String.format("Limit type = %s", limitType));
        this.addLog(String.format("Upper limit = %s", uperLimit));
        this.addLog(String.format("Lower limit = %s", lowerLimit));
    }

    public FuncAllConfig getAllConfig() {
        return allConfig;
    }

    public String getLimitsErrorCode() {
        return this.data.getString(AllKeyWord.CONFIG.ERROR_CODE);
    }

    public String getErrorDes() {
        return this.data.getString(AllKeyWord.CONFIG.ERROR_DES);
    }

    public String getLocalErrorCode() {
        return this.data.getString(AllKeyWord.SFIS.ERRORCODE);
    }

    public String getLocalErrorDes() {
        return this.data.getString(AllKeyWord.SFIS.ERRORDES);
    }

    boolean isDone() {
        return status == 2;
    }
}
