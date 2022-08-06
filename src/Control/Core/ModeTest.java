/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataSource.AbsJsonSource;
import Model.DataTest.ErrorLog;
import Model.DataTest.InputData;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeElement;
import Model.Interface.IFunction;
import Model.Interface.IUpdate;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UIStatus.UiStatus;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit, IUpdate {

    private final List<IFunction> inits;
    private final UIManager uIManager;
    private final ModeTestSource testSource;

    public ModeTest(ModeElement modeInfo, Core core) {
        this.inits = new ArrayList<>();
        this.uIManager = core.getUiManager();
        this.testSource = new ModeTestSource(modeInfo);
        addInitFunctions(this.inits, modeInfo.getIniFunc());
    }

    @Override
    public String toString() {
        return this.testSource.getModeName();
    }

    public String getModeType() {
        return this.testSource.getModeType();
    }

    @Override
    public boolean init() {
        for (IFunction iInit : inits) {
            iInit.run();
            if (!iInit.isPass()) {
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
            uiStatus.setInput(inputData);
            uiStatus.setUnitTest(createCellTest(uiStatus));
        }
    }

    private boolean checkIndex(InputData inputData) {
        if (isIndexEmpty(inputData)) {
            if (this.testSource.isMultiThread()) {
                return getIndex(inputData);
            } else {
                inputData.setIndex("main");
                return true;
            }
        }
        return true;
    }

    private CellTest createCellTest(UiStatus uiStatus) {
        CellTest cellTest = new CellTest(uiStatus, getCheckFunctions(),
                getTestFunctions(uiStatus),
                getListEnd());
        return cellTest;
    }

    public ModeElement getModeConfig() {
        return this.testSource.getModeConfig();
    }

    private void addInitFunctions(List<IFunction> list, List<String> functions) {
        for (String type : functions) {
            list.add(this.factory.getInitFunc(type));
        }
    }

    private void addFunctions(List<AbsFunction> list, List<String> functions) {
        for (String type : functions) {
            AbsFunction func = this.factory.getFunc(type, type);
            if (func != null) {
                list.add(func);
            }
        }
    }

    private List<AbsFunction> getCheckFunctions() {
        List<AbsFunction> functions = new ArrayList<>();
        addFunctions(functions, getFunctionInit());
        return functions;
    }

    private List<AbsFunction> getTestFunctions(UiStatus uiStatus) {
        List<AbsFunction> functions = new ArrayList<>();
        List<String> funcSelected = uiStatus.getFunctionSelected();
        if (this.testSource.isDiscreteTest() && funcSelected != null && !funcSelected.isEmpty()) {
            addFunctions(functions, funcSelected);
        } else {
            addFunctions(functions, getTestFunction());
        }
        return functions;
    }

    private List<AbsFunction> getListEnd() {
        List<AbsFunction> functions = new ArrayList<>();
        addFunctions(functions, getFuntionEnd());
        return functions;
    }

    private boolean isIndexEmpty(InputData inputData) {
        String index = inputData.getIndex();
        return index == null || index.isBlank() || !uIManager.isIndexFree(index);
    }

    private boolean getIndex(InputData inputData) throws HeadlessException {
        String index = JOptionPane.showInputDialog("Nhập vị trí");
        if (uIManager.isIndexFree(index)) {
            inputData.setIndex(index);
            return true;
        }
        return false;
    }

    public List<String> getTestFunction() {
        return this.functionConfig.getFunctionTest();
    }

    public List<String> getFunctionItemTest() {
        return this.functionConfig.getFunctionItemTest();
    }

    public List<String> getFunctionInit() {
        return this.functionConfig.getFunctionInit();
    }

    public List<String> getFuntionEnd() {
        return this.functionConfig.getFuntionEnd();
    }

    @Override
    public boolean update() {
        return updateFunctionsConfig();
    }

    private boolean updateFunctionsConfig() throws HeadlessException {
        if (!checkAmbitConfig()) {
            JOptionPane.showMessageDialog(null, "Update functionsConfig failed!");
            return false;
        }
        if (!checkStationName()) {
            JOptionPane.showMessageDialog(null,
                    "Station setting and station functionsConfig different!");
            return false;
        }
        return true;
    }

    private boolean checkAmbitConfig() {
        var filePath = this.testSource.getFuncConfigPath();
        if (filePath == null) {
            return false;
        }
        AbsJsonSource source = FunctionConfig.getInstance().setPath(filePath);
        return (source != null && source.init());
    }

    private boolean checkStationName() {
        String settingStation = this.testSource.getStationName();
        String ambitConfigStation = FunctionConfig.getInstance().getStationName();
        if (settingStation == null || ambitConfigStation == null) {
            return false;
        }
        return settingStation.equalsIgnoreCase(ambitConfigStation);
    }

}
