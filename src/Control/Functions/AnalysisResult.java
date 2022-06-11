/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.AllKeyWord;
import Model.DataTest.DataBoxs.ItemTestData;
import Model.DataTest.ErrorLog;
import Model.DataTest.FuncAllConfig;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AnalysisResult {

    private final ItemTestData itemTestData;
    private final FuncAllConfig allConfig;

    public AnalysisResult(ItemTestData itemTestData, FuncAllConfig allConfig) {
        this.itemTestData = itemTestData;
        this.allConfig = allConfig;
    }

    public void checkResult(boolean status, String result) {
        if (isResultAvailable(result) && isLimitAvailable()) {
            checkResultWithLimits(result);
        } else {
            this.itemTestData.setPass(status);
        }
    }

    private boolean isLimitAvailable() {
        return allConfig.limitFileAvailable()
                || allConfig.funcConfigAvailable();
    }

    private boolean isResultAvailable(String result) {
        return result != null && !result.isBlank();
    }
    

    private void checkResultWithLimits(String result) {
        String type = allConfig.getTestType();
        if (type.equals(AllKeyWord.MACTH)) {
            checkMatchType(result);
        }else if(type.equals(AllKeyWord.LIMIT)){
            checkLimitType(result);
        }
    }

    private void checkMatchType(String result) {
        try {
            if (getMatch(result, AllKeyWord.LOWER_LIMIT)) {
                this.itemTestData.setPass(true);
                return;
            }
            if (getMatch(result, AllKeyWord.UPPER_LIMIT)) {
                this.itemTestData.setPass(true);
                return;
            }
            this.itemTestData.setPass(false);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            this.itemTestData.setPass(false);
        }

    }

    private boolean getMatch(String result, String key) {
        List<String> limits = allConfig.getListSlip(key, "\\|");
        if (limits != null && !limits.isEmpty()) {
            for (String spec : limits) {
                if (!spec.equals(result)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void checkLimitType(String result) {
        
    }
}
