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
    private final Limit limit;
    private final ErrorCodeElement localErrorCode;
    private final String itemName;

    public FuncAllConfig(UiStatus uiStatus, FunctionConfig functionConfig, String itemName) {
        this.wareHouse = new DataWareHouse();
        this.limit = uiStatus.getModeTest().getModeTestSource().getLimit();
        if (itemName == null) {
            this.itemName = functionConfig.getItemName();
        } else {
            this.itemName = itemName;
        }
        this.localErrorCode = findLocalErrorCode(uiStatus, getBaseItem(this.itemName));
        this.wareHouse.putAll(functionConfig.getJson());
        getAllValueOfLimit(findLimit(getBaseItem(this.itemName)));
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
        if (this.localErrorCode == null) {
            return null;
        }
        return this.localErrorCode.getErrorType(type);
    }

    private void getAllValueOfLimit(LimitElement limitElement) {
        this.wareHouse.put(AllKeyWord.TEST_NAME, this.itemName);
        if (limitElement != null) {
            this.wareHouse.putAll(limitElement.getJson());
        }
    }

    public Limit getLimits() {
        return this.limit;
    }

    public String getFunctionName() {
        return this.wareHouse.getString(AllKeyWord.CONFIG.FUNC_NAME);
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getString(String key) {
        return wareHouse.getString(key);
    }

    public Object getObject(String key) {
        return wareHouse.get(key);
    }

    public <T> List<T> getJsonList(String key) {
        return wareHouse.getListJsonArray(key);
    }

    public List<String> getListSlip(String key, String regex) {
        return wareHouse.getListSlip(key, regex);
    }

    public String getTestType() {
        return wareHouse.getString(AllKeyWord.CONFIG.LIMIT_TYPE);
    }

    public boolean isSpecAvailable() {
        return isSpecConfig() && specAvailable();
    }

    private boolean isSpecConfig() {
        Integer required = this.wareHouse.getInteger(AllKeyWord.CONFIG.REQUIRED);
        String limitType = this.wareHouse.getString(AllKeyWord.CONFIG.LIMIT_TYPE);
        return (required != null && required > 0) && (limitType != null && !limitType.isBlank());
    }

    private boolean specAvailable() {
        String lowLimit = this.wareHouse.getString(AllKeyWord.CONFIG.LOWER_LIMIT);
        String upperLimit = this.wareHouse.getString(AllKeyWord.CONFIG.UPPER_LIMIT);
        return (lowLimit != null && !lowLimit.isBlank())
                || (upperLimit != null && !upperLimit.isBlank());
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

    public String getString(String key, String defaulValue) {
        String value = getString(key);
        return value == null ? defaulValue : value;
    }

    public <T> List<T> getJsonList(String localNameFail, List<T> defaultValue) {
        List<T> value = getJsonList(localNameFail);
        return value == null ? defaultValue : value;
    }

    public boolean isCheckWithSpec() {
        return this.wareHouse.getBoolean(AllKeyWord.CONFIG.IS_CHECK_SPEC, true);
    }
}
