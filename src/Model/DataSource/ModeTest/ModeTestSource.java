/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest;

import Model.DataSource.ModeTest.ErrorCode.ErrorCodeSource;
import Model.DataSource.ModeTest.FunctionConfig.AmbitConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataSource.Setting.ModeElement;
import Model.DataSource.Setting.Setting;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeTestSource {

    private final Limit limit;
    private final ErrorCodeSource errorCode;
    private final AmbitConfig functionConfig;
    private final ModeElement modeConfig;

    public ModeTestSource(ModeElement modeInfo) {
        this.modeConfig = modeInfo;
        this.functionConfig = new AmbitConfig();
        this.limit = new Limit();
        this.errorCode = ErrorCodeSource.getInstance();
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

    public FunctionConfig getFunctionsConfig(FunctionName item) {
        return this.functionConfig.getElement(item);
    }

    public List<FunctionName> getItemTestFunctions() {
        return this.functionConfig.getDebugFunctions();
    }

    public boolean updateFunctionsConfig() throws HeadlessException {
        String configFile = modeConfig.getAmbitConfigPath();
        if (!checkAmbitConfig(configFile)) {
            JOptionPane.showMessageDialog(null, 
                    "Update functionsConfig failed! Local file: " + configFile);
            return false;
        }
        if (!checkStationName()) {
            JOptionPane.showMessageDialog(null,
                    "Station setting and station in functionsConfig different! Local file: " + configFile);
            return false;
        }
        return true;
    }
    
    public boolean updateLimitsConfig() throws HeadlessException {
        String pathFile = modeConfig.getLocalLimitPath();
        String command = Setting.getInstance().getUpdateLimitCommand();
        if (!updateLimit(pathFile,command )) {
            String mess = String.format("Update limits failed!\r\nLocal: %s\r\ncommand: %s", pathFile, command);
            JOptionPane.showMessageDialog(null, mess);
            return false;
        }
        return true;
    }

    private boolean updateLimit(String path, String cmd) {
        if (this.limit == null) {
            return false;
        }
        if (cmd == null) {
            JOptionPane.showMessageDialog(null, "no set limit for test.");
            return true;
        }
        this.limit.setPath(path);
        this.limit.setUpdateCommand(cmd);
        return this.limit.init();
    }

    private boolean checkAmbitConfig(String file) {
        if (file == null || this.functionConfig == null) {
            return false;
        }
        this.functionConfig.setPath(file);
        return this.functionConfig.init();
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
        return this.functionConfig.getSelectedItem(listItem);
    }
}
