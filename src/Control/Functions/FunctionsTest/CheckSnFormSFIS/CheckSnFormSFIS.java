/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CheckSnFormSFIS;

import Control.Functions.AbsFunction;
import Model.DataModeTest.DataBoxs.ItemTestData;
import Model.DataModeTest.ErrorLog;
import Model.DataModeTest.InputData;
import Model.DataSource.PcInformation;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class CheckSnFormSFIS extends AbsFunction {

    public static final String FAIL_PC = "FAILEDPC";
    public static final String DEBUG_PC = "DEBUGPC";
    public static final String ERROR_CODE = "ERRORCODE";
    public static final String ERROR_DES = "ERRORDES";
    public static final String COUNTTEST = "COUNTTEST";
    public static final String MODE = "MODE";
    public static final String SN = "SN";
    public static final String MLBSN = "MLBSN";
    public static final String PCNAME = "PCNAME";
    public static final String STATUS = "STATUS";
    private static final String RESULT = "result";
    private final SfisAPI sfisAPI;

    public CheckSnFormSFIS(String itemName) {
        super(itemName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        String url = funcConfig.getValue("URL_CHECK_SN");
        addLog("send to url: " + url);
        String command = getComand();
        addLog(command);
        String response = this.sfisAPI.sendToSFIS(url, command);
        addLog(response);
        return checkResponse(response);
    }

    private String getComand() {
        JSONObject command = new JSONObject();
        InputData inputData = uIData.getInputData();
        command.put(SN, inputData.getInput());
        command.put(PCNAME, PcInformation.getInstance().getPcName());
        return command.toJSONString();
    }

    private boolean checkResponse(String response) {
        if (response.contains(SfisAPI.SEND_SFIS__EXCEPTION)) {
            this.uIData.setMessage(response);
            return false;
        }
        ItemTestData itemTest = new ItemTestData(this.getItemName());
        try {
            JSONObject res = JSONObject.parseObject(response);
            if (res.getString(RESULT).equals("PASS")) {
                JSONObject data = res.getJSONObject("data");
                for (String key : this.funcConfig.getListString("DATA_FORMAT")) {
                    this.uIData.putProductInfo(key, data.getString(key));
                }
            } else {
                this.uIData.setMessage(res.getString("message"));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.uIData.setMessage(e.getMessage());
            return false;
        }
        itemTest.setIsPass(true);
        return this.dataBox.addItemtest(itemTest);
    }

}
