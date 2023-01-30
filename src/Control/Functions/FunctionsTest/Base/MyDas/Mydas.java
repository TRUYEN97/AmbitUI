/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.MyDas;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Mydas extends AbsFunction {
    private final FileBaseFunction fileBaseFunction;

    public Mydas(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.fileBaseFunction = new FileBaseFunction(functionParameters, itemName);
    }

    public Mydas(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        String IP = this.config.getString("IP");
        addLog("CONFIG", String.format(String.format("IP: %s", IP)));
        String pcName = this.processData.getString("pcname");
        addLog("CONFIG", String.format(String.format("PC name: %s", pcName)));
        String uutModel = this.config.getString("product");
        addLog("CONFIG", String.format(String.format("Product: %s", uutModel)));
        String station = this.processData.getString(AllKeyWord.STATION_TYPE);
        addLog("CONFIG", String.format(String.format("Station: %s", station)));
        String pn = this.processData.getString("pnname");
        addLog("CONFIG", String.format(String.format("PN name %s", pn)));
        MydasClient mydasClient = new MydasClient(IP, pcName, "3.0", "3.0", uutModel, station, pn);
        String ftpPath = craeteFtpPath();
        this.productData.put("ftppath", ftpPath);
        if (mydasClient.connectPts() != 1) {
            addLog("connect PTS (Mydas) failed!");
            return false;
        } else {
            String detail = "NA,NA,0;";
            ItemTestData itemTestData = this.processData.getFirstFail();
            String errorInfo = String.format("%s,%s,|", itemTestData.getLocalErrorCode(), itemTestData.getLocalErrorDes());
            String mainInfo = getMainInfo();
            mydasClient.initClientInfo();
            mydasClient.setData(detail, 0);
            mydasClient.setData(errorInfo, 1);
            mydasClient.setData(mainInfo, 2);
            addLog("------------mydas----------------");
            addLog("DETAILINFO=[" + detail + "]");
            addLog("ERRORINFO=[" + errorInfo + "]");
            addLog("MAININFO=[" + mainInfo + "]");
            if (mydasClient.sendPTS() == 0) {
                addLog("Send to pts failed....");
                return true;
            } else {
                addLog("Send to pts Passed");
                return false;
            }
        }
    }

    private String getMainInfo() {
        StringBuilder builder = new StringBuilder();
        List<String> listKey = this.config.getListJsonArray("MydasElem");
        addLog("elem", listKey);
        if (listKey == null || listKey.isEmpty()) {
            addLog("elem is null or empty!");
            return null;
        }
        for (String key : listKey) {
            String value = this.processData.getString(key);
            if (value == null) {
                continue;
            }
            if (key.equalsIgnoreCase(AllKeyWord.SFIS.STATUS)) {
                builder.append(value.equals(ItemTestData.PASS) ? 1 : 0);
            } else {
                builder.append(value);
            }
            builder.append(",");
        }
        return builder.toString();
    }
     private String craeteFtpPath() {
        List<String> elementName = this.config.getListJsonArray("FtpName");
        List<String> elementPath = this.config.getListJsonArray("FtpPath");
        String dir = this.fileBaseFunction.createDirPath(elementPath);
        String name = this.fileBaseFunction.createNameFile(elementName, config.getString("FtpType"));
        return String.format("%s/%s", dir, name);
    }

}
