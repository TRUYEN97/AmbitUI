/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;

/**
 *
 * @author 21AK22
 */
public class DataCore {

    private final StringBuffer inputData;

    public DataCore() {
        this.inputData = new StringBuffer();
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

}
