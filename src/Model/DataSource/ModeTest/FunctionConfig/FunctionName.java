/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

/**
 *
 * @author Administrator
 */
public class FunctionName {
    private final String itemName;
    private final String function;

    public FunctionName(String itemName, String function) {
        this.itemName = itemName;
        this.function = function;
    }

    public String getItemName() {
        return itemName;
    }

    public String getFunctionName() {
        return function;
    }

    @Override
    public String toString() {
        return itemName;
    }
    
}
