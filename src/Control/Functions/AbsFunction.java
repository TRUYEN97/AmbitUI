/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.Core;
import Control.Core.ModeTest;
import Model.DataModeTest.InputData;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.ManagerUI.UIManager;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements Runnable {

    protected UIManager uIManager;
    protected ModeTest modeTest;
    protected FunctionConfig funtionConfig;
    private boolean pass;

    public void setCore(Core core) {
        if (core == null) {
            return;
        }
        this.uIManager = core.getUiManager();
    }

    public boolean isPass() {
        return pass;
    }

    protected void setPass(boolean pass) {
        this.pass = pass;
    }
}
