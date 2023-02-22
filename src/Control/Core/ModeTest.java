/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataSource.ModeTest.ModeTestSource;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IFunction;
import Model.Interface.IUpdate;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit, IUpdate {

    private final List<String> inits;
    private final ModeTestSource testSource;

    public ModeTest(ModeElement modeInfo) {
        this.inits = modeInfo.getIniFunc();
        this.testSource = new ModeTestSource(modeInfo);
    }

    @Override
    public String toString() {
        return getModeName();
    }

    public String getModeName() {
        return this.testSource.getModeName();
    }

    public String getModeType() {
        return this.testSource.getModeType();
    }

    public ModeTestSource getModeTestSource() {
        return testSource;
    }

    @Override
    public boolean init() {
        Factory factory = Factory.getInstance();
        for (var funcName : inits) {
            IFunction funcInit = factory.getInitFunc(funcName);
            if (funcInit == null) {
                return false;
            }
            funcInit.run();
            if (!funcInit.isPass()) {
                return false;
            }
        }
        return true;
    }

    public ModeElement getModeConfig() {
        return this.testSource.getModeConfig();
    }

    @Override
    public boolean update() {
        return this.testSource.updateFunctionsConfig()
                && this.testSource.updateLimitsConfig();
    }

    public boolean canDebug() {
        return testSource.canDebug();
    }

    public boolean isUseDHCP() {
        return testSource.isUseDHCP();
    }

    public Color getTestColor() {
        return testSource.getTestColor();
    }

    public String getStationType() {
        return testSource.getStationType();
    }
}
