/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.Core;
import Control.Core.ModeTest;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.FunctionConfig.FunctionElement;
import Model.DataSource.Limit.Limit;
import Model.ManagerUI.UIData;
import Model.ManagerUI.UIInput;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected UIManager uIManager;
    protected ModeTest modeTest;
    protected final FunctionElement funcConfig;
    protected final Limit limit;
    protected UiStatus uiStatus;
    protected boolean isPass;
    private UIData data;
    private UIInput input;
    private AbsSubUi subUi;

    protected AbsFunction(String FunctionName) {
        this.funcConfig = FunctionConfig.getInstance().getElement(FunctionName);
        this.limit = Limit.getInstance();
        this.isPass = false;
    }

    public void setCore(Core core) {
        if (core == null) {
            return;
        }
        this.uIManager = core.getUiManager();
    }

    public void setUIStatus(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.data = uiStatus.getData();
        this.input = uiStatus.getInput();
        this.subUi = uiStatus.getSubUi();
    }

    @Override
    public void run() {
        if (isModeSkip()) {
            this.data.setResult("Canceled");
            isPass = true;
            return;
        }
        for (int times = 0; times < getRetry(); times++) {
            if (test()) {
                isPass = true;
                return;
            }
        }
        isPass = false;
    }

    private boolean isModeSkip() {
        String modeSkip = this.funcConfig.getModeSkip();
        return modeSkip != null && modeSkip.isBlank() && modeSkip.equals(modeTest.toString());
    }

    public abstract boolean test();

    @Override
    public boolean isPass() {
        return isPass;
    }

    public long getTimeOut() {
        return funcConfig.getTimeOut();
    }

    public boolean isMutiTasking() {
        if (funcConfig == null) {
            return false;
        }
        return this.funcConfig.isMutiTasking();
    }

    private int getRetry() {
        if (funcConfig != null) {
            return funcConfig.getRetry();
        }
        return 1;
    }
}
