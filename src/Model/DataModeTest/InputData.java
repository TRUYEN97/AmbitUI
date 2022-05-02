/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;

/**
 *
 * @author 21AK22
 */
public class InputData {

    private final StringBuffer inputData;
    private String index;

    public InputData() {
        this.inputData = new StringBuffer();
        this.index = new String();
    }

    public void setInput(String input) {
        if (!this.inputData.isEmpty()) {
            this.inputData.delete(0, this.inputData.length());
        }
        this.inputData.append(input);
    }

    public String getInput() {
        return this.inputData.toString();
    }

    public String getIndex() {
        if (this.index == null) {
            return "";
        }
        return this.index;
    }

    public void setIndex(String index) {
        if (index == null) {
            return;
        }
        this.index = index;
    }

}
