/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SFIS.CheckSnFormSFIS;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.SFIS.SfisFunctions;

/**
 *
 * @author Administrator
 */
public class CheckSnFormSFIS extends AbsFunction {

    private SfisFunctions sfis;

    public CheckSnFormSFIS(String itemName) {
        super(itemName);
    }

    @Override
    public boolean test() {
        this.sfis = new SfisFunctions(this);
        String url = funcConfig.getValue("URL_CHECK_SN");
        addLog("send to url: " + url);
        String command = this.sfis.createCommand();
        addLog("command: " + command);
        if (command == null) {
            addLog("command is null ");
            return false;
        }
        String response = this.sfis.sendToSFIS(url, command);
        addLog(response);
        return this.sfis.checkResponse(response);
    }

}
