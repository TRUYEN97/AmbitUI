/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CreateTxtLog;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CreateTxt extends AbsFunction {

    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public CreateTxt(FunctionParameters parameters) {
        this(parameters,  null);
    }
    
    public CreateTxt(FunctionParameters parameters, String item) {
        super(parameters,  item);
        this.analysisBase = new AnalysisBase(parameters,  item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        return saveTxtFile() && saveFileZip();
    }

    private boolean saveFileZip() {
        addLog("Save file zip!");
        try {
            String filePath = this.allConfig.getString("localFile");
            List<String> elementName = this.allConfig.getListJsonArray("ElementName");
            String zipFile = this.fileBaseFunction.createNameFile(elementName, ".zip");
            String txtFile = this.fileBaseFunction.createNameFile(elementName, ".txt");
            return this.fileBaseFunction.saveZip(filePath, zipFile, txtFile);
        } catch (Exception e) {
            addLog("Save file zip failed: " + e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean saveTxtFile() {
        addLog("Save file txt!");
        MyLoger loger = new MyLoger();
        try {
            String filePath = this.allConfig.getString("localFile");
            List<String> elementName = this.allConfig.getListJsonArray("ElementName");
            String txtFile = this.fileBaseFunction.createNameFile(elementName, ".txt");
            String path = String.format("%s/%s", filePath, txtFile);
            if (!loger.begin(new File(path), true, true)) {
                addLog("Error", "Open file log failed!");
                return false;
            }
            createInfo(loger);
            for (FunctionData dataBox : processData.getDataBoxs()) {
                if (dataBox.isTesting()) {
                    continue;
                }
                addLog(" - add item: " + dataBox.getFunctionName());
                loger.add(dataBox.getLog());
                loger.add("//////////////////////////////////////////////////////////////////\r\n");
            }
            addLog("Save file txt in " + path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            addLog("Save file txt failed: " + e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            loger.close();
        }
    }

    private void createInfo(MyLoger loger) {
        loger.add("===================================================================\r\n");
        loger.add(String.format("Start at = %s\r\n", processData.getString(AllKeyWord.START_TIME)));
        loger.add(String.format("End test at = %s\r\n", processData.getString(AllKeyWord.FINISH_TIME)));
        loger.add(String.format("Status = %s\r\n", processData.getString(AllKeyWord.STATUS)));
        loger.add(String.format("Test time = %s s\r\n", processData.getString(AllKeyWord.CYCLE_TIME)));
        loger.add(String.format("Final test time = %.3f s\r\n", processData.getRuntime()));
        loger.add(String.format("Station = %s\r\n", processData.getString(AllKeyWord.STATION_NAME)));
        loger.add(String.format("Localtion = %s\r\n", processData.getString(AllKeyWord.INDEX)));
        loger.add(String.format("HHSN = %s\r\n", processData.getString(AllKeyWord.SN)));
        loger.add(String.format("DEVICESN = %s\r\n", processData.getString(AllKeyWord.MLBSN)));
        loger.add("===================================================================\r\n");
    }

}
