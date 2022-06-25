/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GenerateNode;

import Control.Functions.AbsFunction;

/**
 *
 * @author 21AK22
 */
public class GenerateNode extends AbsFunction {
    
    public GenerateNode(String FunctionName) {
        super(FunctionName);
    }
    
    @Override
    public boolean test() {
        String send = this.allConfig.getString("SEND_FORMAT");
        String read = this.allConfig.getString("DATA_FORMAT");
        if (send == null || send.isBlank() || read == null || read.isBlank()) {
            addLog("config missing \"SEND_FORMAT\" and \"DATA_FORMAT\"");
            return false;
        }
        addLog("UI->SFIS:" + send);
        addLog("SFIS -> UI:" + read);
        return true;
    }
    
}
