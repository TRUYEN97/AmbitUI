/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.Core;
import Control.Core.ModeTest;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.Limit.Limit;
import Model.ManagerUI.UIData;
import Model.ManagerUI.UIInput;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UiStatus;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements Runnable {

    protected UIManager uIManager;
    protected ModeTest modeTest;
    protected final FunctionConfig funtionConfig;
    protected final Limit limit;
    private String name;
    protected UiStatus uiStatus;
    private UIData Data;
    private UIInput input;
    private AbsSubUi subUi;
    private boolean pass;

    protected AbsFunction() {
        this.funtionConfig = FunctionConfig.getInstance();
        this.limit = Limit.getInstance();
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

    public boolean isPass() {
        return pass;
    }

    protected void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMutiTasking() {
        return this.funtionConfig.getElemnt(name).isMutiTasking();
    }
}
