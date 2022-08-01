/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Control.Functions.AbsFunction;
import FileTool.FileService;

/**
 *
 * @author Administrator
 */
public class VoltageTest extends AbsFunction{

    public VoltageTest(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        String path = this.allConfig.getString("FileVoltageItems");
        String fileVoltage = new FileService().readFile("");
    }
    
}
