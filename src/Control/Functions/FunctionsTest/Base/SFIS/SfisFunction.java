/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.SFIS;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.ErrorLog;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class SfisFunction extends AbsFunction {

    private final SfisAPI sfisAPI;
    private static final String DATA_FORMAT = "DATA_FORMAT";
    private static final String SEND_FORMAT = "SEND_FORMAT";

    public SfisFunction(String functionName) {
        super(functionName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        String url = this.allConfig.getString("URL_CHECK_SN");
        String type = this.allConfig.getString("SFIS_TYPE");
        if (url == null || url.isBlank()) {
            addLog("URL_CHECK_SN is null or emtpty!");
            return false;
        }
        if (type == null || type.isBlank()) {
            addLog("Check SN....");
        }else{
            addLog("Send test result to sfis");
        }
        addLog("send to url: " + url);
        String command = this.createCommand();
        addLog("command: " + command);
        if (command == null) {
            addLog("command is null ");
            return false;
        }
        String response = this.sfisAPI.sendToSFIS(url, command);
        addLog("Response is: " + response);
        if (type == null) {
            return checkResponse(response);
        } else {
            return checkFinalResponse(response);
        }
    }

    private String createCommand() {
        JSONObject command = new JSONObject();
        List<String> listKey = this.allConfig.getListSlip(SEND_FORMAT, "\\|");
        addLog(SEND_FORMAT, listKey);
        if (listKey == null || listKey.isEmpty()) {
            addLog(DATA_FORMAT + " is null or empty!");
            return null;
        }
        for (String key : listKey) {
            String value = productData.getString(key);
            if (value != null) {
                command.put(key.toUpperCase(), value);
            } else {
                return null;
            }
        }
        return command.toJSONString();
    }

    public boolean checkResponse(String response) {
        if (response == null) {
            this.addLog("response is null!");
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.uiData.setMessage(response);
            return false;
        }
        JSONObject res = JSONObject.parseObject(response);
        if (res.getString(AllKeyWord.RESULT).equals("PASS")) {
            JSONObject data = res.getJSONObject(AllKeyWord.DATA);
            List<String> listKey = this.allConfig.getListSlip(DATA_FORMAT, "\\|");
            addLog(DATA_FORMAT, listKey);
            if (listKey == null || listKey.isEmpty()) {
                addLog(DATA_FORMAT + " is null or empty!");
                return false;
            }
            addLog("add data to production info");
            for (String key : listKey) {
                if (data.containsKey(key)) {
                    String value = data.getString(key);
                    addLog(String.format("add key: %s -- Value: %s", key, value));
                    this.productData.put(key, value);
                } else {
                    addLog(String.format("Not have \"%s\" in sfis data", key));
                    return false;
                }
            }
            return true;
        } else {
            this.addLog(res.getString("message"));
            this.uiData.setMessage(res.getString("message"));
            return false;
        }
    }

    private boolean checkFinalResponse(String response) {
        if (response == null) {
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.uiData.setMessage(response);
            return false;
        }
        try {
            JSONObject res = JSONObject.parseObject(response);
            this.uiData.setMessage(res.getString(AllKeyWord.MESSAGE));
            return res.getString(AllKeyWord.RESULT).equals("PASS");
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.uiData.setMessage(e.getMessage());
            return false;
        }
    }
}
