/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.Limit.LimitElement;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FuncAllConfig {

    private final DataWareHouse wareHouse;
    private final String itemName;
    private FunctionElement functionConfig;
    private LimitElement limitElement;
    private ErrorCodeElement localErrorCode;

    public FuncAllConfig(String itemName) {
        this.wareHouse = new DataWareHouse();
        this.itemName = itemName;
    }

    public void setResources(UiStatus uiStatus) {
        this.functionConfig = uiStatus.getModeTest().getModeTestSource().getFunctionsConfig(itemName);
        this.limitElement = findLimit(uiStatus, itemName);
        this.localErrorCode = uiStatus.getModeTest().
                getModeTestSource().getErrorCodeSource().getElement(itemName);
        getAllValueOfConfig();
        getAllValueOfLimit();
    }

    private LimitElement findLimit(UiStatus uiStatus, String itemName) {
        if (itemName.matches(CHECK) && getLimit(uiStatus, itemName) == null) {
            String newItemName = itemName.substring(0, itemName.lastIndexOf("_"));
            return getLimit(uiStatus, newItemName);
        }
        return getLimit(uiStatus, itemName);
    }

    private LimitElement getLimit(UiStatus uiStatus, String nameItem) {
        return uiStatus.getModeTest().
                getModeTestSource().getLimit().getElement(nameItem);
    }
    private static final String CHECK = ".+_[0-9]+$";

    public JSONObject getLocalErrorCode(String type) {
        if (localErrorCode == null) {
            String mess = String.format("Missing error code of %s - %s type !!",
                    this.functionConfig.getItemName(), type);
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError(this, mess);
            return null;
        }
        return this.localErrorCode.getErrorType(type);
    }

    private void getAllValueOfLimit() {
        if (this.limitElement == null) {
            return;
        }
        this.wareHouse.putAll(limitElement.getJson());
    }

    private void getAllValueOfConfig() {
        this.wareHouse.putAll(this.functionConfig.getJson());
    }

    public String getFunctionName() {
        return this.functionConfig.getFunctionName();
    }

    public String getString(String key) {
        return wareHouse.getString(key);
    }

    public Object getObject(String key) {
        return wareHouse.get(key);
    }

    public List<String> getListJsonArray(String key) {
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
        return itemName;
    }

    public Integer getInteger(String key) {
        return this.wareHouse.getInteger(key);
    }

    public Integer getInteger(String key, int defaultV) {
        Integer value = getInteger(key);
        return value == null ? defaultV : value;
    }
}
