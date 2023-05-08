/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckProduct;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Model.DataTest.FunctionParameters;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CheckProduct extends AbsFunction {

    private final AnalysisBase analysisBase;

    public CheckProduct(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.analysisBase = new AnalysisBase(functionParameters, itemName);
    }

    public CheckProduct(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        List<JSONObject> elems = this.config.getJsonList("elems");
        if (elems == null || elems.isEmpty()) {
            addLog("PC", "Nothing to check!");
            return true;
        }
        addLog(LOG_KEYS.PC, "key - value - type - target");
        addLog(LOG_KEYS.PC, "......................................");
        ConditionModel model;
        for (JSONObject elem : elems) {
            model = new ConditionModel(elem);
            String key = model.getKey();
            String target = model.getTarget();
            String mess = model.getMessage();
            String type = model.getType(ConditionModel.EQUAL);
            String value = this.processData.getString(key);
            addLog(LOG_KEYS.PC, "%s - %s - %s - %s",
                    key, value, type, target);
            if ((value != null || target != null) && !isTrueTarget(value, target, type)) {
                addLog("PC", "Message: %s", mess);
                this.processData.setMessage(mess);
                return false;
            }
            addLog(LOG_KEYS.PC, "......................................");
        }
        addLog("PC", "Check done!");
        return true;
    }

    private boolean isTrueTarget(String value, String targets, String type) {
        if (targets == null || type == null || value == null) {
            return false;
        }
        switch (type) {
            case ConditionModel.CONTAIN -> {
                if (isContain(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.EQUAL -> {
                if (isEqual(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.START_WITH -> {
                if (isStartWith(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.END_WITH -> {
                if (isEndWith(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.LARGER -> {
                return isLarger(value, targets);
            }

            case ConditionModel.SMALLER -> {
                return isSmaller(value, targets);
            }

            case ConditionModel.NOT_CONTAIN -> {
                if (!isContain(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.NOT_EQUAL -> {
                if (!isEqual(targets, value)) {
                    return true;
                }
            }
            case ConditionModel.NOT_START_WITH -> {
                if (!isStartWith(targets, value)) {
                    return true;
                }
            }

            case ConditionModel.NOT_END_WITH -> {
                if (!isEndWith(targets, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSmaller(String value, String targets) {
        if (!this.analysisBase.isNumber(value)) {
            addLog(LOG_KEYS.PC, "The value needs to be a number!");
            return false;
        }
        if (!this.analysisBase.isNumber(targets)) {
            addLog(LOG_KEYS.PC, "The target needs to be a number!");
            return false;
        }
        double val = this.analysisBase.string2Double(value);
        double target = this.analysisBase.string2Double(targets);
        return val < target;
    }

    private boolean isLarger(String value, String targets) {
        if (!this.analysisBase.isNumber(value)) {
            addLog(LOG_KEYS.PC, "The value needs to be a number!");
            return false;
        }
        if (!this.analysisBase.isNumber(targets)) {
            addLog(LOG_KEYS.PC, "The target needs to be a number!");
            return false;
        }
        double val = this.analysisBase.string2Double(value);
        double target = this.analysisBase.string2Double(targets);
        return val > target;
    }

    private boolean isEndWith(String targets, String value) {
        for (String spec : targets.split(SEPARATOR)) {
            if (value.trim().endsWith(spec.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStartWith(String targets, String value) {
        for (String spec : targets.split(SEPARATOR)) {
            if (value.trim().startsWith(spec.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEqual(String targets, String value) {
        for (String spec : targets.split(SEPARATOR)) {
            if (value.trim().equalsIgnoreCase(spec.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isContain(String targets, String value) {
        for (String spec : targets.split(SEPARATOR)) {
            if (value.contains(spec)) {
                return true;
            }
        }
        return false;
    }
    private static final String SEPARATOR = ",|\\|";
}
