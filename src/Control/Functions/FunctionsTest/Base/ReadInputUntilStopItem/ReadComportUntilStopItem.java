/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.ReadInputUntilStopItem;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Administrator
 */
public class ReadComportUntilStopItem extends AbsFunction {

    private final FunctionBase functionBase;
    private ExecutorService executorService;
    private Future future;

    public ReadComportUntilStopItem(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.functionBase = new FunctionBase(functionParameters, itemName);
        this.executorService = Executors.newSingleThreadExecutor();
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
            stopThreadpoolFuture();
            this.future = this.executorService.submit(() -> {
                while (comport.isConnect()) {
                    String data = comport.readLine();
                    if (data == null) {
                        continue;
                    }
                    addLog(LOG_KEYS.COMPORT, data);
                }
            });
            while (comport.isConnect()) {
                ItemTestData target;
                target = processData.getItemTestData(itemTarget);
                if (target != null) {
                    stopThreadpoolFuture();
                    return true;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            addLog(LOG_KEYS.ERROR, "COM disconnected!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.executorService.shutdown();
        }
    }

    private void stopThreadpoolFuture() {
        while (this.future != null && !this.future.isDone()) {
            this.future.cancel(true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }

    private ComPort getSenderType() {
        String com = this.functionBase.getComportName();
        int baudrate = this.config.getInteger("baudrate", 9600);
        addLog(LOG_KEYS.COMPORT, "comport: %s -- baudrate: %s", com, baudrate);
        return this.functionBase.getComport(com, baudrate);
    }

}
