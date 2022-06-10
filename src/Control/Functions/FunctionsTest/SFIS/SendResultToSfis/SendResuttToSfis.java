/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SFIS.SendResultToSfis;

import Control.Functions.AbsFunction;
import Model.DataTest.ErrorLog;
import Model.DataTest.InputData;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class SendResuttToSfis extends AbsFunction {

    private final SfisAPI sfisAPI;

    public SendResuttToSfis(String itemName) {
        super(itemName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        String url = this.allConfig.getString("URL_SEND_RESULT");
        addLog("send to url: " + url);
        String command = createCommand();
        addLog("command = " + command);
        if (command == null) {
            return false;
        }
        String response = this.sfisAPI.sendToSFIS(url, command);
        addLog(response);
        return checkFinalResponse(response);
    }

    private String createCommand() {
        JSONObject command = new JSONObject();
        List<String> listKey = this.allConfig.getListString("SEND_FORMAT");
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
            this.uiData.setMessage(res.getString(InputData.MESSAGE));
            return res.getString(InputData.RESULT).equals("PASS");
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.uiData.setMessage(e.getMessage());
            return false;
        }
    }
}
