/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.TxtLog.ZipLog;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ZipFile extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;

    public ZipFile(FunctionParameters parameters) {
        this(parameters, null);
    }

    public ZipFile(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        return saveFileZip();
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

}
