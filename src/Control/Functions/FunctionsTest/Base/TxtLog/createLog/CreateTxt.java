/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.TxtLog.CreateLog;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import MyLoger.MyLoger;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CreateTxt extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;

    public CreateTxt(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CreateTxt(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        return saveTxtFile();
    }

    private boolean saveTxtFile() {
        addLog("Save file txt!");
        try {
            MyLoger loger = new MyLoger();
            String filePath = this.config.getString("localFile");
            List<String> elementName = this.config.getListJsonArray("ElementName");
            String txtFile = this.fileBaseFunction.createNameFile(elementName, ".txt");
            String path = String.format("%s/%s", filePath, txtFile);
            addLog("DIR", path);
            loger.setFile(new File(path));
            loger.setSaveMemory(true);
            loger.clear();
            createInfo(loger);
            int id = 0;
            for (FunctionData dataBox : processData.getDataBoxs()) {
                loger.add(String.format("//////////////////////////- ID[%s] -//////////////////////////\r\n", id++));
                addLog(" - add item: " + dataBox.getFunctionName());
                String log = dataBox.getLog();
                loger.add(log == null ? "\r\n" : log);
                loger.add("//////////////////////////////////////////////////////////////\r\n");
            }
            addLog("PC", "Save file txt at " + path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            addLog("Save file txt failed: " + e.getLocalizedMessage());
            ErrorLog.addError(this, e.getLocalizedMessage());
            return false;
        }
    }

    private void createInfo(MyLoger loger) throws IOException {
        loger.add("===================================================================\r\n");
        loger.add(String.format("Start at = %s\r\n", processData.getString(AllKeyWord.START_TIME)));
        loger.add(String.format("End test at = %s\r\n", processData.getString(AllKeyWord.FINISH_TIME)));
        loger.add(String.format("Status = %s\r\n", processData.getString(AllKeyWord.SFIS.STATUS)));
        if (isFailded()) {
            loger.add(String.format("Error code = %s\r\n", processData.getString(AllKeyWord.CONFIG.ERROR_CODE)));
            loger.add(String.format("Error des = %s\r\n", processData.getString(AllKeyWord.CONFIG.ERROR_DES)));
            loger.add(String.format("Local error code = %s\r\n", processData.getString(AllKeyWord.SFIS.ERRORCODE)));
            loger.add(String.format("Local error des = %s\r\n", processData.getString(AllKeyWord.SFIS.ERRORDES)));
        }
        loger.add(String.format("Test time = %s s\r\n", processData.getString(AllKeyWord.CYCLE_TIME)));
        loger.add(String.format("Final test time = %.3f s\r\n", processData.getRuntime()));
        loger.add(String.format("Station = %s\r\n", processData.getString(AllKeyWord.STATION_NAME)));
        loger.add(String.format("Localtion = %s\r\n", processData.getString(AllKeyWord.INDEX)));
        loger.add(String.format("HHSN = %s\r\n", processData.getString(AllKeyWord.SFIS.SN)));
        loger.add(String.format("DEVICESN = %s\r\n", processData.getString(AllKeyWord.SFIS.MLBSN)));
        loger.add("===================================================================\r\n");
    }

    private boolean isFailded() {
        String status = processData.getString(AllKeyWord.SFIS.STATUS);
        return status != null && status.equalsIgnoreCase(ItemTestData.FAIL);
    }

}