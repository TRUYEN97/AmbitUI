/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataModeTest.InputData;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeInfo;
import Model.Factory.Factory;
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
public class ModeTest implements IInit, Runnable {

    private final ModeInfo modeInfo;
    private final List<AbsFunction> inits;
    private final Factory factory;
    private final FunctionConfig functionConfig;
    private final UIManager uIManager;
    private InputData inputData;
    private UnitTest unitTest;
    private UiStatus uiStatus;

    public ModeTest(ModeInfo info, Core core) {
        this.inits = new ArrayList<>();
        this.factory = Factory.getInstance();
        this.functionConfig = FunctionConfig.getInstance();
        this.modeInfo = info;
        this.uIManager = core.getUiManager();
        addFunctions(this.inits, this.modeInfo.getIniFunc());
    }

    @Override
    public String toString() {
        return modeInfo.getModeName();
    }

    @Override
    public boolean init() {
        for (AbsFunction iInit : inits) {
            iInit.run();
            if (!iInit.isPass()) {
                return false;
            }
        }
        return true;
    }

    public boolean setInput(InputData inputData) {
        if (inputData != null) {
            this.inputData = inputData;
            return true;
        }
        return false;
    }

    private boolean check() {
        if (isIndexEmpty(inputData)) {
            if (isMutiThread()) {
                inputData.setIndex("main");
                return true;
            } else if (getIndex(inputData)) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        if (check()) {
            uiStatus = uIManager.getUiStatus(inputData.getIndex());
            unitTest = new UnitTest(inputData);
            unitTest.setCheckFunction(getCheckFunctions());
            unitTest.setTestFunction(getTestFunctions());
            unitTest.setEndFunction(getEndFunctions());
            uiStatus.setUnitTest(unitTest);
        }
        inputData = null;
        unitTest = null;
        uiStatus = null;
    }

    public ModeInfo getModeInfo() {
        return this.modeInfo;
    }

    private void addFunctions(List<AbsFunction> list, List<String> functions) {
        for (String type : functions) {
            list.add(this.factory.getFunc(type));
        }
    }

    private List<AbsFunction> getCheckFunctions() {
        List<AbsFunction> functions = new ArrayList<>();
        addFunctions(functions, this.modeInfo.getCheckFunctions());
        return functions;
    }

    private List<AbsFunction> getTestFunctions() {
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

    private boolean isMutiThread() {
        return this.modeInfo.isMutiThread();
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
