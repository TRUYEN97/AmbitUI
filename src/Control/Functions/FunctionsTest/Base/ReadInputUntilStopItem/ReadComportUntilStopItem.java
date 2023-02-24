/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.ReadInputUntilStopItem;

import Communicate.ISender;
import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class ReadComportUntilStopItem extends AbsFunction {

    private final FunctionBase functionBase;

    public ReadComportUntilStopItem(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.functionBase = new FunctionBase(functionParameters, itemName);
    }

    public ReadComportUntilStopItem(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        try ( ComPort comport = getSenderType()) {
            if (comport == null) {
                return false;
            }
            String command = this.config.getString("Command");
            if (command != null && this.functionBase.sendCommand(comport, command)) {
                return false;
            }
            String itemTarget = this.config.getString("itemTarget");
            addLog(LOG_KEYS.CONFIG, "itemTarget: %s", itemTarget);
            if (itemTarget == null) {
                return false;
            }
            ItemTestData target;
            while (true) {
                target = this.processData.getItemTestData(itemTarget);
                if (target != null) {
                    break;
                }
                addLog(LOG_KEYS.COMPORT, comport.readLine());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ComPort getSenderType() {
        String com = this.functionBase.getComportName();
        int baudrate = this.config.getInteger("baudrate", 9600);
        addLog(LOG_KEYS.COMPORT, "comport: %s -- baudrate: %s", com, baudrate);
        return this.functionBase.getComport(com, baudrate);
    }

}
