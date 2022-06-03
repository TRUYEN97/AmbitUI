/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SFIS;

import Control.Functions.AbsFunction;
import Model.DataModeTest.ErrorLog;
import Model.DataModeTest.InputData;
import Model.DataSource.FunctionConfig.FunctionElement;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SfisFunctions {

    private final SfisAPI sfisAPI;
    private final FunctionElement funcConfig;
    private final UiData uiData;
    private final AbsFunction function;

    public SfisFunctions(AbsFunction function) {
        this.sfisAPI = new SfisAPI();
        this.funcConfig = function.getFuncConfig();
        this.uiData = function.getUiData();
        this.function = function;
    }

    public String createCommand() {
        JSONObject command = new JSONObject();
        List<String> listKey = this.funcConfig.getListString("SEND_FORMAT");
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
            this.function.addLog("response is null!");
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.uiData.setMessage(response);
            return false;
        }
        try {
            JSONObject res = JSONObject.parseObject(response);
            if (res.getString(InputData.RESULT).equals("PASS")) {
                JSONObject data = res.getJSONObject(InputData.DATA);
                for (String key : this.funcConfig.getListString("DATA_FORMAT")) {
                    this.uiData.putProductInfo(key, data.getString(key));
                }
                return true;
            } else {
                this.function.addLog(res.getString("message"));
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

    public boolean checkFinalResponse(String response) {
        if (response == null) {
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.uiData.setMessage(response);
            return false;
        }
        try {
            JSONObject res = JSONObject.parseObject(response);
            this.uiData.setMessage(res.getString(InputData.MESSAGE));
            return res.getString(InputData.RESULT).equals("PASS");
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.uiData.setMessage(e.getMessage());
            return false;
        }
    }

    public String sendToSFIS(String url, String command) {
        return this.sfisAPI.sendToSFIS(url, command);
    }
}
