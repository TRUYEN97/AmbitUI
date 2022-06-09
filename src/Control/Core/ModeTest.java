/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataTest.ErrorLog;
import Model.DataTest.InputData;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IFunction;
import Model.Interface.IUpdate;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UIStatus.UiStatus;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit, IUpdate {

    private final List<String> inits;
    private final UIManager uIManager;
    private final ModeTestSource testSource;

    public ModeTest(ModeElement modeInfo, Core core) {
        this.inits = modeInfo.getIniFunc();
        this.uIManager = core.getUiManager();
        this.testSource = new ModeTestSource(modeInfo);
    }

    @Override
    public String toString() {
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

    public void runTest(InputData inputData) {
        if (inputData != null && checkIndex(inputData)) {
            if (!uIManager.getUiStatus(inputData.getIndex()).update()) {
                String mess = String.format("%s update mode fail!", inputData.getIndex());
                ErrorLog.addError(this, mess);
                JOptionPane.showMessageDialog(null, mess);
            }
            UiStatus uiStatus = uIManager.getUiStatus(inputData.getIndex());
            uiStatus.startTest(inputData, this.testSource);
        }
    }

    private boolean checkIndex(InputData inputData) {
        if (isIndexEmpty(inputData)) {
            if (this.testSource.isMultiThread()) {
                getIndex(inputData);
            } else {
                inputData.setIndex("main");
            }
        }
        return this.uIManager.isIndexFree(inputData.getIndex());

    }

    public ModeElement getModeConfig() {
        return this.testSource.getModeConfig();
    }

    private boolean isIndexEmpty(InputData inputData) {
        String index = inputData.getIndex();
        return index == null || index.isBlank();
    }

    private boolean getIndex(InputData inputData) throws HeadlessException {
        String index = JOptionPane.showInputDialog("Nhập vị trí");
        if (uIManager.isIndexFree(index)) {
            inputData.setIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean update() {
        return this.testSource.updateFunctionsConfig();
    }
}
