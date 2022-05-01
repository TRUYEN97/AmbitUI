/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.Core;
import Control.Core.ModeTest;
import Model.DataModeTest.DataCore;
import Model.DataSource.FunctionConfig.FuntionConfig;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements Runnable {

    protected DataCore dataCore;
    protected ModeTest modeTest;
    protected FuntionConfig funtionConfig;
    private boolean pass;

    public void setCore(Core core) {
        if (core == null) {
            return;
        }
        this.dataCore = core.getDataCore();
    }

    public boolean isPass() {
        return pass;
    }

    protected void setPass(boolean pass) {
        this.pass = pass;
    }
}
