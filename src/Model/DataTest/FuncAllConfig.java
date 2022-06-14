/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.Limit.LimitElement;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FuncAllConfig {

    private final DataWareHouse wareHouse;
    private final String functionName;
    private FunctionElement functionConfig;
    private LimitElement limitElement;

    public FuncAllConfig(String functionName) {
        this.wareHouse = new DataWareHouse();
        this.functionName = functionName;
    }

    public void setResources(UiStatus uiStatus, FunctionElement functionConfig) {
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
        return wareHouse.getString(AllKeyWord.LIMIT_TYPE);
    }

    public boolean isLimitAvailable() {
        return limitFileAvailable() || funcConfigAvailable();
    }

    private boolean limitFileAvailable() {
        return this.limitElement != null
                && this.limitElement.getInteger(AllKeyWord.REQUIRED) != null
                && this.limitElement.getInteger(AllKeyWord.REQUIRED) > 0
                && (!this.limitElement.getString(AllKeyWord.LOWER_LIMIT).isBlank()
                || !this.limitElement.getString(AllKeyWord.UPPER_LIMIT).isBlank());
    }

    private boolean funcConfigAvailable() {
        return this.functionConfig != null
                && this.functionConfig.getString(AllKeyWord.LIMIT_TYPE) != null
                && (!this.functionConfig.getString(AllKeyWord.LOWER_LIMIT).isBlank()
                || !this.functionConfig.getString(AllKeyWord.UPPER_LIMIT).isBlank());
    }

    public String getItemName() {
        return this.functionConfig.getItemName();
    }
}
