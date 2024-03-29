/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.SFIS;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.ItemTestData;
import Model.ErrorLog;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;
import DHCP.DhcpData;
import Model.DataTest.FunctionParameters;
import MyLoger.MyLoger;
import Time.TimeBase;
import java.io.File;
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
    private static final MyLoger sfisLog = new MyLoger();

    public SfisFunction(FunctionParameters functionName) {
        this(functionName, null);
    }

    public SfisFunction(FunctionParameters functionName, String item) {
        super(functionName, item);
        this.sfisAPI = new SfisAPI();
    }

    private synchronized static void writeLog(String location, String log, Object... param) throws Exception {
        sfisLog.setFile(new File(String.format("../sfislog/%s.txt", new TimeBase().getDate())));
        sfisLog.setSaveMemory(true);
        sfisLog.addLog(location, log, param);
    }

    @Override
    public boolean test() {
        try {
            String url = this.config.getString("URL");
            String type = this.config.getString("SFIS_TYPE");
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
            writeLog("TE->API", String.format("%s %s", this.uIInfo.getName(), command));
            String response = this.sfisAPI.sendToSFIS(url, command);
            writeLog("API->TE", String.format("%s %s", this.uIInfo.getName(), response));
            addLog("Response is: " + response);
            if (type == null) {
                return checkResponse(response);
            } else {
                return checkFinalResponse(response);
            }
        } catch (Exception e) {
            addLog(LOG_KEYS.ERROR, e.getMessage());
            return false;
        }
    }

    private String getCommand(String type) {
        String command;
        String status = this.processData.getString(AllKeyWord.SFIS.STATUS);
        if (type == null || (status != null && status.equals(ItemTestData.PASS))) {
            command = this.createCommand(SEND_FORMAT);
        } else {
            command = this.createCommand(SEND_FORMAT_FAIL);
        }
        return command;
    }

    private String createCommand(String keyWord) {
        JSONObject command = new JSONObject();
        List<String> listKey = this.config.getJsonList(keyWord);
        int maxLength = this.config.getInteger("MaxLength", -1);
        addLog("Input", "Input: " + this.processData.getString(AllKeyWord.SFIS.SN));
        addLog("Config", "MaxLength: " + maxLength);
        addLog(keyWord, listKey);
        if (listKey == null || listKey.isEmpty()) {
            addLog(keyWord + " is null or empty!");
            return null;
        }
        for (String key : listKey) {
            String value = this.processData.getString(key);
            key = key.toUpperCase();
            addLog("key: " + key + " = " + value);
            if (value == null) {
                continue;
            }
            if (key.equalsIgnoreCase(AllKeyWord.SFIS.PC_NAME)) {
                String location = this.processData.getString(AllKeyWord.POSITION, "");
                command.put(key, location.isBlank() ? value : String.format("%s-%s", value, location));
            } else if (key.equalsIgnoreCase(AllKeyWord.SFIS.STATUS)) {
                command.put(key, value.equals(ItemTestData.PASS) ? PASS : FAIL);
            } else if (maxLength != -1
                    && key.equalsIgnoreCase(AllKeyWord.SFIS.SN)
                    && value.length() > maxLength) {
                command.put(key, value.substring(0, maxLength));
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
            JSONObject data = res.getJSONObject(AllKeyWord.SFIS.DATA);
            List<String> listKey = this.config.getJsonList(DATA_FORMAT);
            addLog(DATA_FORMAT, listKey);
            if (!listKey.isEmpty() && !dataContainKeys(data, listKey)) {
                addLog("Error", "Sfis is not enough data.");
                return false;
            }
            if (!getDataToProductInfo(data)) {
                return false;
            }
            try {
                if (this.modeTest.isUseDHCP() && !putMacDHCP()) {
                    addLog("Get MAC from SFIS for DHCP failed!");
                    return false;
                }
            } catch (Exception e) {
                addLog(LOG_KEYS.ERROR, e.getLocalizedMessage());
                return false;
            }
            addLog("add data to production info done.");
            return true;
        } else {
            this.addLog(res.getString(AllKeyWord.MESSAGE));
            this.processData.setMessage(res.getString(AllKeyWord.MESSAGE));
            this.subUI.showSfisMessager(res.getString(AllKeyWord.MESSAGE));
            return false;
        }
    }

    private boolean getDataToProductInfo(JSONObject data) {
        String value;
        for (String key : data.keySet()) {
            if (key.equals(AllKeyWord.SFIS.MAC)) {
                value = createTrueMac(getValueInData(data, key));
            } else {
                value = getValueInData(data, key);
            }
            addLog(String.format("add key: %s -- Value: %s", key, value));
            this.productData.put(key, value);
        }
        return true;
    }

    private String createTrueMac(String value) {
        if (value.contains(":")) {
            return value;
        }
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

    private boolean putMacDHCP() throws Exception {
        String mac = this.processData.getString(AllKeyWord.SFIS.MAC);
        String oldIP = DhcpData.getInstance().getIP(mac);
        if (mac == null || mac.isBlank()
                || !DhcpData.getInstance().put(mac, uIInfo.getID())) {
            return false;
        }
        if (oldIP != null) {
            addLog("PC", "Old IP: %s in DHCP data", oldIP);
        }
        addLog("PC", "add Mac: %s -- Ip: %s to DHCP data",
                mac, DhcpData.getInstance().getIP(mac));
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
            this.subUI.showSfisMessager(res.getString(AllKeyWord.MESSAGE));
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

    private boolean dataContainKeys(JSONObject data, List<String> listKey) {
        for (String key : listKey) {
            if (!data.containsKey(key)) {
                addLog("Error", "Sfis not contain key: " + key);
                return false;
            }
        }
        return true;
    }
}
