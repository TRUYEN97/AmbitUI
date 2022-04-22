/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;

/**
 *
 * @author Administrator
 */
public class DataInputMode {
    private String snInput;
    private String index;
    private final StringBuilder builder;

    public DataInputMode() {
        this.builder = new StringBuilder();
    }

    public StringBuilder getBuilder() {
        return builder;
    }
    

    public String getSnInput() {
        return snInput;
    }

    public String getIndex() {
        return index;
    }

    public void setSnInput(String snInput) {
        this.snInput = snInput;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    
}
