/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.TxtLog.ZipLog;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;

/**
 *
 * @author Administrator
 */
public class ZipFile extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;
    private String zipPath;
    private String txtpath;

    public ZipFile(FunctionParameters parameters) {
        this(parameters, null);
    }

    public ZipFile(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    public boolean test() {
        return saveFileZip();
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public void setTxtpath(String txtpath) {
        this.txtpath = txtpath;
    }

    private boolean saveFileZip() {
        addLog("Save file zip!");
        try {
            String suffix = this.config.getString("LocalSuffix", "txt");
            String txtFile = this.txtpath == null ? this.fileBaseFunction.createDefaultStringPath(this.processData.isPass()) : txtpath;
            String zipFile = this.zipPath == null ? txtFile.replaceAll(".".concat(suffix), ".zip") : zipPath;
            return this.fileBaseFunction.saveZip(zipFile, txtFile);
        } catch (Exception e) {
            addLog("Save file zip failed: " + e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

}
