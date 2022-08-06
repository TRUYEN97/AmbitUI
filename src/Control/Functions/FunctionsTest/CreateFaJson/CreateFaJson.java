/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CreateFaJson;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.CreateFaJson.KeyWordFaAPI.FUNC_KEY;
import Model.DataTest.InputData;
import Model.DataSource.Tool.FileService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class CreateFaJson extends AbsFunction {

    CreateFaJson(String name) {
        super(name);
    }

    @Override
    public boolean test() {
        JSONObject data = getFaJsonData();
        if (data == null) {
            return false;
        }
        addLog(data.toJSONString());
        JSONObject baseData = createBaseData();
        baseData.put("tests", createFunctionData(data));
        return saveToFile(baseData);
    }

    private boolean saveToFile(JSONObject baseData) {
        String filePath = funcConfig.getValue("LOCAL_FILE");
        String nameFile = createNameFile();
        if (saveFile(filePath, nameFile, baseData.toJSONString())) {
            addLog(String.format("Save json file at %s done!", nameFile));
            return putNameFileToSignal(nameFile);
        }
        addLog(String.format("Save json file at %s failed", nameFile));
        return false;
    }

    private boolean putNameFileToSignal(String nameFile) {
        String keywowk = this.funcConfig.getValue("JsonPathKey");
        if (keywowk == null) {
            addLog("Key of FilePath is null!");
            return false;
        }
        this.uiData.putToSignal(keywowk, nameFile);
        addLog(String.format("put \"%s\" with \"%s\" key to signal",
                nameFile, keywowk));
        return true;
    }

    private String createNameFile() {
        String serial = uiData.getProductInfo(InputData.MLBSN);
        serial = serial.replace('\\', '_');
        serial = serial.replace('/', '_');
        String pcName = uiData.getProductInfo(InputData.PCNAME);
        String mode = uiData.getProductInfo(InputData.MODE);
        return String.format("%s_%s_%s", serial, pcName, mode);
    }

    private JSONObject getFaJsonData() {
        String key = this.funcConfig.getValue("KEY_WORD");
        addLog("Get FA json data in signal!");
        addLog("Get with key: " + key);
        var data = this.uiData.getSignal(key);
        if (data == null) {
            addLog("FA json data == null!");
            return null;
        }
        return (JSONObject) data;
    }

    private JSONObject createBaseData() {
        JSONObject baseData = new JSONObject();
        addLog("Create base data ");
        for (var key : KeyWordFaAPI.BASE_KEY.values()) {
            String value = this.uiData.getProductInfo(key.getInputKey());
            addTo(value, baseData, key.toString());
        }
        baseData.put("status", "passed");
        addToKeyValueLog("status", "passed");
        baseData.put("mode", "debug");
        addToKeyValueLog("mode", "debug");
        addLog("Create base data done!");
        return baseData;
    }

    private JSONArray createFunctionData(JSONObject data) {
        JSONArray funcData = new JSONArray();
        addLog("Create function!");
        for (String location : data.keySet()) {
            funcData.add(createLocalData(location, data));
        }
        addLog("Create function data done!");
        return funcData;
    }

    private JSONObject createLocalData(String location, JSONObject data) {
        JSONObject localtionData;
        localtionData = new JSONObject();
        addLog("Create data for location: " + location);
        getValueOfProductData(localtionData);
        getDataOfLocation(data.getJSONObject(location), localtionData);
        localtionData.put("child_unlinked", "False");
        addToKeyValueLog("child_unlinked", "False");
        String stationType = getStionType(uiData.getProductInfo(InputData.FAIL_PC));
        localtionData.put("failed_station_type", stationType);
        addToKeyValueLog("failed_station_type", stationType);
        String result = getResult(localtionData);
        localtionData.put("post_repair_result", result);
        addToKeyValueLog("post_repair_result", result);
        return localtionData;
    }

    private void getDataOfLocation(JSONObject locationData, JSONObject funcData) {
        for (var key : FUNC_KEY.values()) {
            Object value = locationData.get(key.getInputKey());
            addTo(value, funcData, key.toString());
        }
    }

    private void addTo(Object value, JSONObject funcData, String key) {
        if (value != null) {
            funcData.put(key, value);
            addToKeyValueLog(key, value);
        }
    }

    private void addToKeyValueLog(String key, Object value) {
        addLog(String.format("  - Key: %s -- Value: %s", key, value));
    }

    private void getValueOfProductData(JSONObject funcData) {
        for (var key : FUNC_KEY.values()) {
            String value = this.uiData.getProductInfo(key.getInputKey());
            addTo(value, funcData, key.toString());
        }
        String count = this.uiData.getProductInfo(InputData.COUNTTEST);
        funcData.put("debug_count", Integer.valueOf(count));

    }

    private String getResult(JSONObject funcData) {
        if (funcData.getString(FUNC_KEY.repair_action.toString()).equals("scrap")) {
            return "scrap";
        } else {
            return "passed";
        }
    }

    private String getStionType(String pcName) {
        return pcName.substring(0, pcName.lastIndexOf("-"));
    }

    private boolean saveFile(String dirPath, String nameFile, String data) {
        String filePath = String.format("%s\\%s.json", dirPath, nameFile);
        addLog(String.format("Save json data at: %s", filePath));
        return new FileService().saveFile(filePath, formatJson(data));
    }

    private String formatJson(String str) {
        str = str.replaceAll("\\},(?!\r\n)", "\r\n},\r\n");
        str = str.replaceAll("\\{(?!\r\n)", "\r\n{\r\n");
        str = str.replaceAll("\\}(?!,)", "\r\n}\r\n");
        str = str.replaceAll(",(?=\")", ",\r\n");
        return str.trim();
    }

}
