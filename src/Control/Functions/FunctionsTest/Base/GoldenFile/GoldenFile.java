/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GoldenFile;

import Control.Functions.AbsFunction;
import DHCP.DhcpData;
import Model.AllKeyWord;
import Model.DataTest.FunctionParameters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
        String snInput = this.processData.getString(AllKeyWord.SFIS.SN);
        addLog("PC", "Input: " + snInput);
        if (!findSNInGoldenFile(goldenFile, snInput)) {
            return false;
        }
        if (this.modeTest.isUseDHCP()) {
            if (!putMacDHCP()) {
                addLog("PC","insert mac into DHCP failded!");
                return false;
            }
            addLog("PC","insert mac into DHCP");
        }
        return true;
    }

    private boolean putMacDHCP() {
        String mac = this.processData.getString(AllKeyWord.SFIS.MAC);
        if (mac == null || mac.isBlank()
                || !DhcpData.getInstance().put(mac, uIInfo.getID())) {
            return false;
        }
        addLog("PC","add ethernetmac: %s -- Ip: %s to DHCP data",
                mac, DhcpData.getInstance().getIP(mac));
        return true;
    }

    private boolean findSNInGoldenFile(File goldenFile, String snInput) {
        try ( BufferedReader fr = new BufferedReader(new FileReader(goldenFile))) {
            String line;
            String[] elems;
            String sn;
            String mlbsn;
            String ethernetmac;
            while ((line = fr.readLine()) != null) {
                if (!line.matches("^.+,.+,.+$")) {
                    continue;
                }
                elems = line.split(",");
                sn = elems[0].trim().toUpperCase();
                mlbsn = elems[1].trim().toUpperCase();
                ethernetmac = createTrueMac(elems[2].trim().toUpperCase());
                if (sn.equalsIgnoreCase(snInput) || mlbsn.equalsIgnoreCase(snInput)) {
                    this.productData.put("sn", sn);
                    addLog("PC", "key: sn - value: %s", sn);
                    this.productData.put("mlbsn", mlbsn);
                    addLog("PC", "key: mlbsns - value: %s",mlbsn);
                    this.productData.put("ethernetmac", ethernetmac);
                    addLog("PC", "key: ethernetmac - value: %s", ethernetmac);
                    return true;
                }
            }
            addLog("PC", "SN \'%s\' is not in the golden file !", snInput);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            addLog("Error", e.getLocalizedMessage());
            return false;
        }
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

}
