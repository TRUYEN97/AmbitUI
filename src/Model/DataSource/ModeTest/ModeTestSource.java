/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest;

import Model.DataSource.ModeTest.ErrorCode.ErrorCodeSource;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataSource.Setting.ModeElement;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeTestSource {

    private Limit limit;
    private ErrorCodeSource errorCode;
    private final FunctionConfig functionConfig;
    private final ModeElement modeConfig;

    public ModeTestSource(ModeElement modeInfo) {
        this.modeConfig = modeInfo;
        this.functionConfig = new FunctionConfig();
    }

    public ModeElement getModeConfig() {
        return modeConfig;
    }

    public String getModeName() {
        return modeConfig.getModeName();
    }

    public boolean isMultiThread() {
        return modeConfig.isMultiThread();
    }

    public String getModeType() {
        return modeConfig.getModeType();
    }

    public boolean isDiscreteTest() {
        return modeConfig.isDiscreteTest();
    }

    public long getTimeOutTest() {
        return functionConfig.getTimeOutTest();
    }

    public List<FunctionName> getTestFunctions() {
        return this.functionConfig.getTestFunctions();
    }

    public List<FunctionName> getCheckFunctions() {
        return this.functionConfig.getCheckFunctions();
    }

    public List<FunctionName> getEndFunctions() {
        return this.functionConfig.getEndFuntions();
    }

    public FunctionElement getFunctionsConfig(FunctionName item) {
        return this.functionConfig.getElement(item);
    }

    public List<FunctionName> getItemTestFunctions() {
        return this.functionConfig.getDebugFunctions();
    }

    public boolean updateFunctionsConfig() throws HeadlessException {
        if (!checkAmbitConfig()) {
            JOptionPane.showMessageDialog(null, "Update functionsConfig failed!");
            return false;
        }
        if (!checkStationName()) {
            JOptionPane.showMessageDialog(null,
                    "Station setting and station functionsConfig are different!");
            return false;
        }
        this.limit = Limit.getInstance();
        this.errorCode = ErrorCodeSource.getInstance();
        return true;
    }

    private boolean checkAmbitConfig() {
        var filePath = modeConfig.getAmbitConfigPath();
        if (filePath == null) {
            return false;
        }
        this.functionConfig.setPath(filePath);
        return (this.functionConfig != null && this.functionConfig.init());
    }

    private boolean checkStationName() {
        String settingStation = functionConfig.getStationName();
        String ambitConfigStation = this.modeConfig.getStationName();
        if (settingStation == null || ambitConfigStation == null) {
            return false;
        }
        return settingStation.equalsIgnoreCase(ambitConfigStation);
    }

    public Limit getLimit() {
        return limit;
    }

    public ErrorCodeSource getErrorCodeSource() {
        return errorCode;
    }

    public int getLoopTest() {
        return modeConfig.getLoopTest();
    }

    public List<FunctionName> getSelectedItem(List<FunctionName> listItem) {
        return  this.functionConfig.getSelectedItem(listItem);
    }
}
