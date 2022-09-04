/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeSource;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataSource.ModeTest.Limit.LimitElement;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FuncAllConfig {

    private static final String CHECK_BASE_ITEM = ".+_[0-9]+$";
    private final DataWareHouse wareHouse;
    private final FunctionConfig functionConfig;
    private final LimitElement limitElement;
    private final Limit limit;
    private final ErrorCodeElement localErrorCode;
    private final String itemName;

    public FuncAllConfig(UiStatus uiStatus, FunctionConfig functionConfig, String itemName) {
        this.wareHouse = new DataWareHouse();
        this.functionConfig = functionConfig;
        this.limit = uiStatus.getModeTest().getModeTestSource().getLimit();
        if (itemName == null) {
            this.itemName = functionConfig.getItemName();
        } else {
            this.itemName = itemName;
        }
        this.limitElement = findLimit(getBaseItem(this.itemName));
        this.localErrorCode = findLocalErrorCode(uiStatus, getBaseItem(this.itemName));
        getAllValueOfConfig();
        getAllValueOfLimit();
    }

    private ErrorCodeElement findLocalErrorCode(UiStatus uiStatus, String itemName) {
        ErrorCodeSource codeSource = uiStatus.getModeTest().getModeTestSource().getErrorCodeSource();
        if (codeSource == null || codeSource.getElement(itemName) == null) {
            String mess = String.format("Missing error code of %s !!",
                    this.itemName);
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError(this, mess);
            return null;
        }
        return codeSource.getElement(itemName);
    }

    private String getBaseItem(String itemName) {
        if (itemName.matches(CHECK_BASE_ITEM) && findLimit(itemName) == null) {
            return itemName.substring(0, itemName.lastIndexOf("_"));
        }
        return itemName;
    }

    private LimitElement findLimit(String nameItem) {
        return this.limit.getElement(nameItem);
    }

    public JSONObject getLocalErrorCode(String type) {
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

    public Limit getLimits() {
        return this.limit;
    }

    public String getFunctionName() {
        return this.functionConfig.getFunctionName();
    }

    public String getItemName() {
        return this.functionConfig.getItemName();
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

    public Integer getInteger(String key) {
        return this.wareHouse.getInteger(key);
    }

    public Integer getInteger(String key, int defaultV) {
        Integer value = getInteger(key);
        return value == null ? defaultV : value;
    }

    public boolean getBoolean(String key) {
        return this.wareHouse.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.wareHouse.getBoolean(key, defaultValue);
    }

    public Set<String> getListItemName() {
        return limit.getListItemName();
    }
}
