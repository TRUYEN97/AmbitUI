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
public class CheckSnFormSFIS extends AbsFunction{
   private final SfisAPI sfisAPI;
           
    public CheckSnFormSFIS( String itemName) {
        super(itemName);
        this.sfisAPI = new SfisAPI();
        this.funcConfig.getValue("URL_CHECK_SN");
    }
    
    @Override
    public boolean test() {
        JSONObject command = new JSONObject();
        command.put("SN", inputData.getInput());
        command.put("PCNAME", inputData.getInput());
        return true;
    }
    
}
