/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.Limit.LimitKeyWord;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataTest.DataBoxs.FunctionData;
import Model.DataTest.ErrorLog;
import Model.DataTest.FuncAllConfig;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected final FuncAllConfig allConfig;
    private FunctionData functionData;
    protected UiData uiData;
    protected AbsSubUi subUi;

    protected AbsFunction(String functionName) {
        this.allConfig = new FuncAllConfig(functionName);
    }

    public void setResuorces(UiStatus uiStatus, FunctionData functionData, FunctionElement funcConfig) {
        this.allConfig.setResuorces(uiStatus, funcConfig);
        this.functionData = functionData;
        this.uiData = uiStatus.getUiData();
        this.subUi = uiStatus.getSubUi();
    }

    @Override
    public void run() {
        boolean status = test();
        if (isResultAvailable() && isLimitAvailable()) {
            checkResultWithLimits(getResult());
        } else {
            this.functionData.setStatus(status);
            setResult(this.functionData.createDefaultResult());
        }
    }

    protected abstract boolean test();

    protected void addLog(Object str) {
        this.functionData.addLog(str);
    }

    private boolean isLimitAvailable() {
        return allConfig.limitFileAvailable()
                || allConfig.funcConfigAvailable();
    }

    private boolean isResultAvailable() {
        return getResult() != null && !getResult().isBlank();
    }

    @Override
    public boolean isPass() {
        return functionData.isPass();
    }

    protected void setResult(String result) {
        this.functionData.setResult(result);
    }

    public String getFuntionName() {
        return this.allConfig.getFunctionName();
    }

    protected String getResult() {
        return this.functionData.getResultTest();
    }

    private void checkResultWithLimits(String result) {
        String type = allConfig.getTestType();
        if (type.equals("BOOL")) {
            checkBoolType(result);
        }
    }

    private void checkBoolType(String result) {
        try {
            if (getMatch(result, LimitKeyWord.LOWER_LIMIT)) {
                this.functionData.setStatus(true);
                return;
            }
            if (getMatch(result, LimitKeyWord.UPPER_LIMIT)) {
                this.functionData.setStatus(true);
                return;
            }
            this.functionData.setStatus(false);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.functionData.setStatus(false);
        }

    }

    private boolean getMatch(String result, String key) {
        List<String> limits = allConfig.getListSlip(key, "\\|");
        if (limits != null && !limits.isEmpty()) {
            for (String spec : limits) {
                if (!spec.equals(result)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
