/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.Delta;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class Delta extends AbsFunction {

    private final AnalysisBase analysisBase;

    public Delta(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.analysisBase = new AnalysisBase(functionParameters, itemName);
    }

    public Delta(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        String itemName = this.config.getString("item");
        addLog("CONFIG", "item: %s", itemName);
        String itemName1 = this.config.getString("item1");
        addLog("CONFIG", "item1: %s", itemName1);
        Double value;
        Double value1;
        if ((value = getValueOf(itemName)) == null || (value1 = getValueOf(itemName1)) == null) {
            return false;
        }
        double delta = value1 - value;
        addLog("PC", "Delta: %s - %s = %s", value1, value, delta);
        setResult(delta);
        return true;
    }

    private Double getValueOf(String itemName) {
        ItemTestData itemTest = this.processData.getItemTestData(itemName);
        if (itemTest == null) {
            addLog("PC", "item %s = null", itemName);
            return null;
        }
        String value = itemTest.getResultTest();
        if (analysisBase.isNumber(value)) {
            return Double.valueOf(value);
        }
        return null;
    }

}
