/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest;

import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataSource.Setting.ModeElement;

/**
 *
 * @author Administrator
 */
public class ModeTestSource {
    private Limit limit;
    private FunctionConfig functionConfig;
    private final ModeElement modeConfig;

    public ModeTestSource(ModeElement modeInfo ) {
        this.modeConfig = modeInfo;
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

    public String getStationName() {
        return functionConfig.getStationName();
    }

    public String getFuncConfigPath() {
        return modeConfig.getAmbitConfigPath();
    }
    
}
