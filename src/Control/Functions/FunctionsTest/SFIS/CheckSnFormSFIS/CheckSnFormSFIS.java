/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SFIS.CheckSnFormSFIS;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataTest.ErrorLog;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CheckSnFormSFIS extends AbsFunction {

    private final SfisAPI sfisAPI;

    public CheckSnFormSFIS(String functionName) {
        super(functionName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        String url = this.allConfig.getString("URL_CHECK_SN");
        addLog("send to url: " + url);
        String command = this.createCommand();
        addLog("command: " + command);
        if (command == null) {
            addLog("command is null ");
            return false;
        }
        String response = this.sfisAPI.sendToSFIS(url, command);
        addLog(response);
        return this.checkResponse(response);
    }

    private String createCommand() {
        JSONObject command = new JSONObject();
        List<String> listKey = this.allConfig.getListSlip("SEND_FORMAT","\\|");
        if (listKey == null || listKey.isEmpty()) {
            return null;
        }
        for (String key : listKey) {
            String value = this.uiData.getProductInfo(key);
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
        try {
            JSONObject res = JSONObject.parseObject(response);
            if (res.getString(AllKeyWord.RESULT).equals("PASS")) {
                JSONObject data = res.getJSONObject(AllKeyWord.DATA);
                for (String key : this.allConfig.getListJsonArray("DATA_FORMAT")) {
                    if (data.containsKey(key)) {
                        String value = data.getString(key);
                        addLog(String.format("add key: %s -- Value: %s", key, value));
                        this.uiData.putProductInfo(key, value);
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
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.uiData.setMessage(e.getMessage());
            return false;
        }
    }

}
