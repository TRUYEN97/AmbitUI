/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.FaAPI.CreateTxtLog;

import Control.Functions.AbsFunction;
import FileTool.FileService;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import MyLoger.MyLoger;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class CreateTxtLog extends AbsFunction {

    private String txtFile;

    public CreateTxtLog(String itemName) {
        super(itemName);
    }

    @Override
    public boolean test() { 
        return saveTxtFile() && saveFileZip();
    }

    private boolean saveTxtFile() {
        addLog("Save file txt!");
        MyLoger loger = new MyLoger();
        try {
            this.txtFile = creatFilePath("serial.txt");
            addLog("file path: " + this.txtFile);
            loger.begin(new File(this.txtFile), true, true);
            for (FunctionData dataBox : processData.getDataBoxs()) {
                if (dataBox.isTesting()) {
                    continue;
                }
                addLog(" - add item: " + dataBox.getItemFunctionName());
                loger.add(dataBox.getLog());
                loger.add("//////////////////////////////////////////////////////////////////\r\n");
            }
            addLog("Save file txt ok!");
            String keyOfFilePath = this.allConfig.getString("FileTxt");
            addLog("Key of file in Signal: " + keyOfFilePath);
            this.testSignal.put(keyOfFilePath, this.txtFile);
            return true;
        } catch (Exception e) {
            addLog("Save file failed: " + e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            loger.close();
        }
    }

    private String creatFilePath(String hauTo) {
        String dir = this.allConfig.getString("LOCAL_FILE");
        return String.format("%s/%s", dir, createNameFile(hauTo));
    }

    private String createNameFile(String hauTo) {
        String serial = productData.getString(AllKeyWord.MLBSN);
        serial = serial.replace('\\', '_');
        serial = serial.replace('/', '_');
        String pcName = productData.getString(AllKeyWord.STATION_NAME);
        String mode = productData.getString(AllKeyWord.MODE);
        return String.format("%s_%s_%s_%s",
                serial, pcName, mode, hauTo);
    }

    private boolean saveFileZip() {
        addLog("Save file zip!");
        try {
            String zipFile = creatFilePath("serial.zip");
            addLog("Save zip file path: " + zipFile);
            addLog("File path: " + this.txtFile);
            return new FileService().zipFile(zipFile, new File(this.txtFile));
        } catch (Exception e) {
            addLog("Save file zip failed: " + e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

}
