/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GoldenFile;

import Control.Functions.AbsFunction;
import DHCP.DhcpData;
import FileTool.FileService;
import Model.AllKeyWord;
import Model.DataSource.Setting.Setting;
import Model.DataTest.FunctionParameters;
import com.alibaba.fastjson.JSONObject;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class GoldenFile extends AbsFunction {

    public GoldenFile(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    @Override
    protected boolean test() {
        String goldenPath = this.config.getString("golden_file");
        addLog("config", goldenPath);
        File goldenFile;
        if (goldenPath == null || !(goldenFile = new File(goldenPath)).exists()) {
            addLog("config", "Golen file not valid!");
            return false;
        }
        JSONObject golden;
        String snInput = this.processData.getString(AllKeyWord.SFIS.SN);
        addLog("Input", "Input: " + snInput);
        try {
            golden = JSONObject.parseObject(new FileService().readFile(goldenFile));
            if (!golden.containsKey(snInput)) {
                addLog("MESSAGE", String.format("SN: %s not exists!", snInput));
                return false;
            }
            JSONObject snGolden = golden.getJSONObject(snInput);
            if (snGolden.isEmpty()) {
                addLog("GOLDEN", String.format("SN golden: %s is empty!", snInput));
                return false;
            }
            for (String key : snGolden.keySet()) {
                this.productData.put(key, snGolden.getString(key));
                addLog("GOLDEN", String.format("key: %s - value: %s", key, snInput));
            }
            if (Setting.getInstance().isOnDHCP() && !putMacDHCP()) {
                addLog("Get MAC from SFIS for DHCP failed!");
                return false;
            }
            addLog("add data to production info done.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
      private boolean putMacDHCP() {
        String mac = this.processData.getString(AllKeyWord.SFIS.MAC);
        if (mac == null || mac.isBlank()
                || !DhcpData.getInstance().put(mac, uIInfo.getID())) {
            return false;
        }
        addLog(String.format("add Mac: %s -- Ip: %s to DHCP data",
                mac, DhcpData.getInstance().getIP(mac)));
        return true;
    }

}
