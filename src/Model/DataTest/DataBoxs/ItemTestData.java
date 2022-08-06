/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

/**
 *
 * @author 21AK22
 */
public class ItemTestData {

    private final String itemTestName;
    private double lowLimit;
    private double upLimit;
    private Object value;
    private String spec;
    private boolean isPass;

    public ItemTestData(String itemTestName) {
        this.itemTestName = itemTestName;
        this.isPass = false;
    }

    public String getItemTestName() {
        return itemTestName;
    }

    public double getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(double lowLimit) {
        this.lowLimit = lowLimit;
    }

    public double getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(double upLimit) {
        this.upLimit = upLimit;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public boolean isIsPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

}
