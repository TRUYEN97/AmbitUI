/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.Limit.LimitElement;
import Model.DataSource.ModeTest.Limit.LimitKeyWord;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FuncAllConfig {

    private final DataWareHouse wareHouse;
    private final String functionName;
    private UiStatus uiStatus;
    private FunctionElement functionConfig;
    private LimitElement limitElement;

    public FuncAllConfig(String functionName) {
        this.wareHouse = new DataWareHouse();
        this.functionName = functionName;
    }

    public void setResuorces(UiStatus uiStatus, FunctionElement functionConfig) {
        this.uiStatus = uiStatus;
        this.functionConfig = functionConfig;
        this.limitElement = uiStatus.getModeTest().
                getModeTestSource().getLimit().getElement(this.functionConfig.getItemName());
        getAllValueOfConfig();
        getAllValueOfLimit();
    }

    private void getAllValueOfLimit() {
        if (this.limitElement == null) {
            return;
        }
        for (String key : this.limitElement.getListItem()) {
            this.wareHouse.put(key, this.limitElement.getString(key));
        }
    }

    private void getAllValueOfConfig() {
        for (String key : this.functionConfig.getListItem()) {
            this.wareHouse.put(key, this.functionConfig.getString(key));
        }
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getString(String key) {
        return wareHouse.getString(key);
    }

    public Object getObject(String key) {
        return wareHouse.get(key);
    }

    public List<String> getListString(String key) {
        return wareHouse.getListJsonArray(key);
    }
    
    public List<String> getListSlip(String key, String regex) {
        return wareHouse.getListSlip(key, regex);
    }

    public String getTestType() {
        return wareHouse.getString(LimitKeyWord.LIMIT_TYPE);
    }
    
    public boolean limitFileAvailable() {
        return this.limitElement != null &&
                this.limitElement.getInteger(LimitKeyWord.REQUIRED) != null &&
                this.limitElement.getInteger(LimitKeyWord.REQUIRED) > 0;
    }
    
    public boolean funcConfigAvailable() {
        return this.functionConfig != null && 
                this.functionConfig.getString(LimitKeyWord.LIMIT_TYPE) != null;
    }
}
