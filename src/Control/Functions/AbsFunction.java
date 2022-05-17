/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.ModeTest;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.FunctionConfig.FunctionElement;
import Model.DataSource.Limit.Limit;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import Model.ManagerUI.UIStatus.Elemants.UISignal;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataModeTest.DataBoxs.DataBox;
import Model.DataModeTest.ErrorLog;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {
    
    protected final FunctionElement funcConfig;
    protected final Limit limit;
    protected boolean isPass;
    private UiData uIData;
    private DataBox dataBox;
    private ModeTest modeTest;
    private UISignal uISignal;
    private AbsSubUi subUi;
    
    protected AbsFunction(String itemName) {
        this.funcConfig = FunctionConfig.getInstance().getElement(itemName);
        this.limit = Limit.getInstance();
        this.isPass = false;
    }
    
    public void setUIStatus(UiStatus uiStatus) {
        this.uIData = uiStatus.getUiData();
        this.dataBox = getDataBox();
        this.uISignal = uiStatus.getUiSignal();
        this.subUi = uiStatus.getSubUi();
        this.modeTest = uiStatus.getModeTest();
    }
    
    public FunctionElement getFuncConfig() {
        return funcConfig;
    }
    
    @Override
    public void run() {
        try {
            if (isModeSkip()) {
                this.dataBox.addLog("Mode Skip: " + modeTest.toString());
                this.dataBox.setResult("Canceled");
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
        } catch (Exception e) {
            ErrorLog.addError(e.getLocalizedMessage());
            this.addLog(e.getMessage());
        }
        
    }

    public void setRsutlt(String result) {
        this.dataBox.setResult(result);
    }
    
    public abstract boolean test();
    
    @Override
    public boolean isPass() {
        return isPass;
    }
    
    public String getItemName() {
        return this.funcConfig.getItemName();
    }
    
    public String getFuncName() {
        return this.funcConfig.getFunctionName();
    }
    
    public void addLog(Object str) {
        this.dataBox.addLog(str);
    }
    
    private int getRetry() {
        if (funcConfig != null) {
            return funcConfig.getRetry();
        }
        return 1;
    }
    
    private DataBox getDataBox() {
        DataBox dataBox = this.uIData.getDataBox(funcConfig.getItemName());
        if (dataBox == null) {
            dataBox = this.uIData.createDataBox(funcConfig.getItemName());
        }
        return dataBox;
    }
    
    private boolean isModeSkip() {
        String modeSkip = this.funcConfig.getModeCancel();
        return modeSkip != null && modeSkip.isBlank() && modeSkip.equals(modeTest.toString());
    }
}
