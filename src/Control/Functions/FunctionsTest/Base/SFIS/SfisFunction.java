/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.SFIS;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataSource.Setting.Setting;
import Model.DataTest.FunctionData.ItemTestData;
import Model.ErrorLog;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import commandprompt.Communicate.DHCP.DhcpData;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class SfisFunction extends AbsFunction {

    private final SfisAPI sfisAPI;
    private static final String DATA_FORMAT = "DATA_FORMAT";
    private static final String SEND_FORMAT = "SEND_FORMAT";
    private static final String SEND_FORMAT_FAIL = "SEND_FORMAT_FAIL";

    public SfisFunction(String functionName) {
        super(functionName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        String url = this.allConfig.getString("URL");
        String type = this.allConfig.getString("SFIS_TYPE");
        if (url == null || url.isBlank()) {
            addLog("URL_CHECK_SN is null or emtpty!");
            return false;
        }
        if (type == null || type.isBlank()) {
            addLog("Check SN....");
        } else {
            addLog("Send test result to sfis");
        }
        addLog("send to url: " + url);
        String command = getCommand(type);
        if (command == null) {
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

    private String getCommand(String type) {
        String command;
        String status = this.processData.getString(AllKeyWord.STATUS);
        if (type == null || (status != null && status.equals(ItemTestData.PASS))) {
            command = this.createCommand(SEND_FORMAT);
        } else {
            command = this.createCommand(SEND_FORMAT_FAIL);
        }
        return command;
    }

    private String createCommand(String keyWord) {
        JSONObject command = new JSONObject();
        List<String> listKey = this.allConfig.getListJsonArray(keyWord);
        addLog(keyWord, listKey);
        if (listKey == null || listKey.isEmpty()) {
            addLog(keyWord + " is null or empty!");
            return null;
        }
        for (String key : listKey) {
            String value = this.processData.getString(key);
            key = key.toUpperCase();
            addLog("key: " + key +" = "+value);
            if (key.equalsIgnoreCase(AllKeyWord.STATUS)) {
                command.put(key,
                        value.equals(ItemTestData.PASS) ? PASS : FAIL);
            } else {
                command.put(key, value);
            }
        }
        addLog("command: " + command);
        return command.toJSONString();
    }

    public boolean checkResponse(String response) {
        if (response == null) {
            this.addLog("response is null!");
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.processData.setMessage(response);
            return false;
        }
        JSONObject res = JSONObject.parseObject(response);
        if (res.getString(AllKeyWord.RESULT).equals(PASS)) {
            List<String> listKey = this.allConfig.getListJsonArray(DATA_FORMAT);
            addLog(DATA_FORMAT, listKey);
            if (listKey == null || listKey.isEmpty()) {
                addLog(DATA_FORMAT + " is null or empty!");
                return false;
            }
            JSONObject data = res.getJSONObject(AllKeyWord.DATA);
            if (!getDataToProductInfo(listKey, data)) {
                return false;
            }
            if (Setting.getInstance().isOnDHCP() && !putMacDHCP()) {
                addLog("Get MAC from SFIS for DHCP failed!");
                return false;
            }
            addLog("add data to production info");
            return true;
        } else {
            this.addLog(res.getString("message"));
            this.processData.setMessage(res.getString("message"));
            return false;
        }
    }

    private boolean getDataToProductInfo(List<String> listKey, JSONObject data) {
        String value;
        for (String key : listKey) {
            if (data.containsKey(key)) {
                if (key.equals(AllKeyWord.MAC)) {
//                    value = createTrueMac(getValueInData(data, key));
                    value = createTrueMac("649714048d60");
                } else {
                    value = getValueInData(data, key);
                }
                addLog(String.format("add key: %s -- Value: %s", key, value));
                this.productData.put(key, value);
            } else {
                addLog(String.format("Not have \"%s\" in sfis data", key));
                return false;
            }
        }
        return true;
    }

    private String createTrueMac(String value) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (char kitu : value.toCharArray()) {
            if (index != 0 && index % 2 == 0) {
                builder.append(':');
            }
            builder.append(kitu);
            index++;
        }
        return builder.toString();
    }

    private String getValueInData(JSONObject data, String key) {
        String value = data.getString(key);
        if (value == null) {
            value = "";
        }
        return value;
    }

    private boolean putMacDHCP() {
        String mac = this.processData.getString(AllKeyWord.MAC);
        if (mac == null || mac.isBlank()) {
            return false;
        }
        DhcpData.getInstance().put(mac, uIInfo.getID());
        addLog(String.format("add Mac: %s -- Ip: %s to DHCP data",
                mac, DhcpData.getInstance().getIP(mac)));
        return true;
    }

    private boolean checkFinalResponse(String response) {
        if (response == null) {
            return false;
        }
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.processData.setMessage(response);
            return false;
        }
        try {
            JSONObject res = JSONObject.parseObject(response);
            this.processData.setMessage(res.getString(AllKeyWord.MESSAGE));
            return res.getString(AllKeyWord.RESULT).equals(PASS);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.processData.setMessage(e.getMessage());
            return false;
        }
    }
    private static final String PASS = "PASS";
    private static final String FAIL = "FAIL";
}
