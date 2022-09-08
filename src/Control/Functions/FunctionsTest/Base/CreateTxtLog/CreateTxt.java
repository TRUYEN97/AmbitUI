/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CreateTxtLog;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionData.FunctionData;
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

    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public CreateTxt(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CreateTxt(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        return saveTxtFile() && saveFileZip();
    }

    private boolean saveFileZip() {
        addLog("Save file zip!");
        try {
            String filePath = this.config.getString("localFile");
            List<String> elementName = this.config.getListJsonArray("ElementName");
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
            String filePath = this.config.getString("localFile");
            List<String> elementName = this.config.getListJsonArray("ElementName");
            String txtFile = this.fileBaseFunction.createNameFile(elementName, ".txt");
            String path = String.format("%s/%s", filePath, txtFile);
            try {
                loger.begin(new File(path), true, true);
            } catch (IOException ex) {
                addLog("Error", ex.getLocalizedMessage());
                ErrorLog.addError(this, ex.getLocalizedMessage());
                return false;
            }
            createInfo(loger);
            for (FunctionData dataBox : processData.getDataBoxs()) {
                addLog(" - add item: " + dataBox.getFunctionName());
                String log = dataBox.getLog();
                loger.add(log == null ? "\r\n" : log);
                loger.add("//////////////////////////////////////////////////////////////////\r\n");
            }
            addLog("Save file txt in " + path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            addLog("Save file txt failed: " + e.getLocalizedMessage());
            ErrorLog.addError(this, e.getLocalizedMessage());
            return false;
        } finally {
            try {
                loger.close();
            } catch (IOException ex) {
                addLog("Error", ex.getLocalizedMessage());
                ErrorLog.addError(this, ex.getLocalizedMessage());
                return false;
            }
        }
    }

    private void createInfo(MyLoger loger) throws IOException {
        loger.add("===================================================================\r\n");
        loger.add(String.format("Start at = %s\r\n", processData.getString(AllKeyWord.START_TIME)));
        loger.add(String.format("End test at = %s\r\n", processData.getString(AllKeyWord.FINISH_TIME)));
        loger.add(String.format("Status = %s\r\n", processData.getString(AllKeyWord.SFIS.SFIS_STATUS)));
        loger.add(String.format("Test time = %s s\r\n", processData.getString(AllKeyWord.CYCLE_TIME)));
        loger.add(String.format("Final test time = %.3f s\r\n", processData.getRuntime()));
        loger.add(String.format("Station = %s\r\n", processData.getString(AllKeyWord.STATION_NAME)));
        loger.add(String.format("Localtion = %s\r\n", processData.getString(AllKeyWord.INDEX)));
        loger.add(String.format("HHSN = %s\r\n", processData.getString(AllKeyWord.SFIS.SFIS_SN)));
        loger.add(String.format("DEVICESN = %s\r\n", processData.getString(AllKeyWord.SFIS.SFIS_MLBSN)));
        loger.add("===================================================================\r\n");
    }

}
