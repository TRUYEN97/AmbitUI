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
    protected final FunctionElement funtionConfig;
    protected final Limit limit;
    protected UiStatus uiStatus;
    protected boolean isPass;
    private UIData Data;
    private UIInput input;
    private AbsSubUi subUi;

    protected AbsFunction(String FunctionName) {
        this.funtionConfig = FunctionConfig.getInstance().getElement(FunctionName);
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
        this.Data = uiStatus.getData();
        this.input = uiStatus.getInput();
        this.subUi = uiStatus.getSubUi();
    }

    @Override
    public void run() {
        for (int times = 0; times < getRetry(); times++) {
            if (test()) {
                break;
            }
        }
    }

    public abstract boolean test();

    @Override
    public boolean isPass() {
        return isPass;
    }

    public boolean isMutiTasking() {
        if (funtionConfig == null) {
            return false;
        }
        return this.funtionConfig.isMutiTasking();
    }

    private int getRetry() {
        if (funtionConfig != null) {
            return funtionConfig.getRetry();
        }
        return 1;
    }
}
