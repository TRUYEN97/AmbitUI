/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CheckSnFormSFIS;

import Control.Functions.AbsFunction;
import SfisAPI17.SfisAPI;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class CheckSnFormSFIS extends AbsFunction {
    
    private final SfisAPI sfisAPI;
    
    public CheckSnFormSFIS(String itemName) {
        super(itemName);
        this.sfisAPI = new SfisAPI();
    }
    
    @Override
    public boolean test() {
        JSONObject command = new JSONObject();
        command.put("SN", uIData.getInputData().getInput());
        command.put("PCNAME", uIData.getInputData().getPcName());
        String url = funcConfig.getValue("URL_CHECK_SN");
        addLog("send to url: " + url);
        addLog(command.toJSONString());
        addLog(this.sfisAPI.sendToSFIS(url,command.toJSONString()));
        return true;
    }
    
}
