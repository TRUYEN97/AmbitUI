/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.MyDas;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import Time.TimeBase;

/**
 *
 * @author Administrator
 */
public class Mydas extends AbsFunction {

    public Mydas(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    public Mydas(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        String IP = this.config.getString("IP");
        addLog("CONFIG", String.format(String.format("IP: %s", IP)));
        boolean sendDetail = this.config.getBoolean("sendDetail", true);
        addLog("CONFIG", String.format(String.format("Send detail: %s", sendDetail)));
        String pcName = this.processData.getString("pcname");
        addLog("CONFIG", String.format(String.format("PC name: %s", pcName)));
        String dutModel = this.processData.getString(AllKeyWord.CONFIG.DUT_MODEL);
        addLog("CONFIG", String.format(String.format("Product: %s", dutModel)));
        String station = this.processData.getString(AllKeyWord.STATION_TYPE);
        addLog("CONFIG", String.format(String.format("Station: %s", station)));
        String pn = this.processData.getString("pnname");
        addLog("CONFIG", String.format(String.format("PN name: %s", pn)));
        String flowVer = this.processData.getString("flowVer", "3.0");
        addLog("CONFIG", String.format(String.format("flowVer: %s", flowVer)));
        String titleVer = this.processData.getString("titleVer", "3.0");
        addLog("CONFIG", String.format(String.format("titleVer: %s", titleVer)));
        MydasClient mydasClient = new MydasClient(IP, pcName, flowVer, titleVer, dutModel, station, pn);
        return sendMydas(mydasClient, sendDetail);
    }

    private boolean sendMydas(MydasClient mydasClient, boolean sendDetail) {
        try {
            if (mydasClient.connectPts() != 1) {
                addLog("connect PTS (Mydas) failed!");
                return false;
            } else {
                addLog("connect PTS (Mydas) ok!");
                String detail = getDetail(sendDetail);
                String errorInfo = getErrorInfo();
                String mainInfo = getMainInfo();
                addLog(LOG_KEYS.PC, "DETAILINFO=[ %s ]", detail);
                addLog(LOG_KEYS.PC, "ERRORINFO=[ %s ]", errorInfo);
                addLog(LOG_KEYS.PC, "MAININFO=[ %s ]", mainInfo);
                if (sendDatasToMyDas(mydasClient, detail, errorInfo, mainInfo)) {
                    addLog("Send to pts Passed");
                    return true;
                }
                addLog("Send to pts failed....");
                return false;
            }
        } finally {
            addLog("------------mydas----------------");
        }
    }

    private boolean sendDatasToMyDas(MydasClient mydasClient, String detail, String errorInfo, String mainInfo) {
        mydasClient.initClientInfo();
        mydasClient.setData(detail, 0);
        mydasClient.setData(errorInfo, 1);
        mydasClient.setData(mainInfo, 2);
        return mydasClient.sendPTS() != 0;
    }

    private String getErrorInfo() {
        ItemTestData itemTestData = this.processData.getFirstFail();
        String errorInfo = "";
        if (itemTestData != null) {
            errorInfo = String.format("%s,%s,%s|", itemTestData.getLocalErrorCode(),
                    itemTestData.getLocalErrorDes(), itemTestData.getResultTest());
        }
        return errorInfo;
    }

    private String getDetail(boolean sendDetail) {
        String detail;
        if (sendDetail) {
            detail = getDetaiLItem();
        } else {
            detail = "NA,NA,0;";
        }
        return detail;
    }

    private String getDetaiLItem() {
        StringBuilder builder = new StringBuilder();
        for (ItemTestData itemTestData : this.processData.getListItemTestData()) {
            if (itemTestData == null) {
                continue;
            }
            builder.append(String.format("%s,%s,%.3f;",
                    itemTestData.isPass() ? "PASS" : "FAIL",
                    itemTestData.getResultTest(),
                    itemTestData.getRunTime()));
        }
        return builder.toString();
    }

    private String getMainInfo() {
        StringBuilder builder = new StringBuilder();
        int status = this.processData.getString(AllKeyWord.SFIS.STATUS, "").equalsIgnoreCase(ItemTestData.PASS)
                ? 1 : 0;
        builder.append(this.processData.getString(AllKeyWord.SFIS.MLBSN, "")).append(",");
        builder.append(status).append(",");
        builder.append(this.processData.getString("ftppath", "")).append(",");
        builder.append(this.processData.getString(AllKeyWord.VERSION, "")).append(",");
        builder.append(this.processData.getString(AllKeyWord.SFIS.PC_NAME, "")).append(",");
        builder.append(this.processData.getString("cycle_time", "")).append(",");
        builder.append(getStartTime()).append(",");
        builder.append(",").append(",").append(this.uIInfo.getName());
        return builder.toString();
    }

    private String getStartTime() {
        return new TimeBase().conVertToFormat(
                this.processData.getString(AllKeyWord.START_TIME, ""),
                TimeBase.SIMPLE_DATE_TIME, TimeBase.MM_DD_YYYY_HH_MM_SS);

    }

}
