/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataModeTest.InputData;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IFunction;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UiStatus;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit {

    private final ModeElement modeInfo;
    private final List<IFunction> inits;
    private final Factory factory;
    private final FunctionConfig functionConfig;
    private final UIManager uIManager;

    public ModeTest(ModeElement info, Core core) {
        this.inits = new ArrayList<>();
        this.factory = Factory.getInstance();
        this.functionConfig = FunctionConfig.getInstance();
        this.modeInfo = info;
        this.uIManager = core.getUiManager();
        addInitFunctions(this.inits, this.modeInfo.getIniFunc());
    }

    @Override
    public String toString() {
        return modeInfo.getModeName();
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
            UiStatus uiStatus = uIManager.getUiStatus(inputData.getIndex());
            uiStatus.setUnitTest(createUnitTest(inputData, uiStatus));
        }
    }

    private boolean checkIndex(InputData inputData) {
        if (isIndexEmpty(inputData)) {
            if (isMultiThread()) {
                return getIndex(inputData);
            } else {
                inputData.setIndex("main");
                return true;
            }
        }
        return true;
    }

    private CellTest createUnitTest(InputData inputData, UiStatus uiStatus) {
        CellTest unitTest = new CellTest(inputData, uiStatus);
        unitTest.setCheckFunction(getCheckFunctions());
        unitTest.setTestFunction(getTestFunctions(uiStatus));
        unitTest.setEndFunction(getEndFunctions());
        return unitTest;
    }

    public ModeElement getModeInfo() {
        return this.modeInfo;
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
        addFunctions(functions, this.modeInfo.getCheckFunctions());
        return functions;
    }

    private List<AbsFunction> getTestFunctions(UiStatus uiStatus) {
        List<AbsFunction> functions = new ArrayList<>();
        if (this.modeInfo.isDiscreteTest()) {
            functions.addAll(uiStatus.getFunctionSelected());
        } else {
            addFunctions(functions, this.functionConfig.getListFunction());
        }
        return functions;
    }

    private List<AbsFunction> getEndFunctions() {
        List<AbsFunction> functions = new ArrayList<>();
        addFunctions(functions, this.modeInfo.getEndFunctions());
        return functions;
    }

    private boolean isMultiThread() {
        return this.modeInfo.isMultiThread();
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
}
