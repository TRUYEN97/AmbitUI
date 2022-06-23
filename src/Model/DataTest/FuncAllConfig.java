/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.Limit.LimitElement;
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
        this.limitElement = uiStatus.getModeTest().
                getModeTestSource().getLimit().getElement(this.functionConfig.getItemName());
        this.localErrorCode = uiStatus.getModeTest().
                getModeTestSource().getErrorCodeSource().getElement(this.functionConfig.getItemName());
        getAllValueOfConfig();
        getAllValueOfLimit();
    }

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
        return this.functionConfig.getItemName();
    }

    public Integer getInteger(String key) {
        return this.wareHouse.getInteger(key);
    }
}
