/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SendResultToSfis;

import Control.Functions.AbsFunction;
import SfisAPI17.SfisAPI;

/**
 *
 * @author 21AK22
 */
public class SendResuttToSfis extends AbsFunction{

    private final SfisAPI sfisAPI;
    public SendResuttToSfis(String itemName) {
        super(itemName);
        this.sfisAPI = new SfisAPI();
    }

    @Override
    public boolean test() {
        return true;
    }
    
}
