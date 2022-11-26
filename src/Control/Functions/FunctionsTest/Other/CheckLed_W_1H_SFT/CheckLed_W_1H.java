/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Other.CheckLed_W_1H_SFT;

import Communicate.Comport.ComPort;
import Communicate.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import FileTool.FileService;
import Model.DataTest.FunctionParameters;
import Time.TimeBase;
import Time.WaitTime.Class.TimeS;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class CheckLed_W_1H extends AbsFunction {

    private final FunctionBase functionBase;
    private CsvLog csvLog;
    private final FileService service;

    public CheckLed_W_1H(FunctionParameters parameter) {
        super(parameter, null);
        this.functionBase = new FunctionBase(parameter);
        this.service = new FileService();
    }

    @Override
    protected boolean test() {
        String timePointKey = this.config.getString("time_point_key");
        var dateOldOb = this.testSignal.getObject(timePointKey);
        String fileNameCSV = this.config.getString("logCSV");
        this.csvLog = new CsvLog(fileNameCSV);
        if (dateOldOb == null) {
            this.csvLog.init();
            checkLed();
            this.testSignal.put(timePointKey, new Date(System.currentTimeMillis()));
            return true;
        }
        if (dateOldOb instanceof Date datePoint) {
            long deta = ((System.currentTimeMillis() - datePoint.getTime()) / 1000);
            long stepTime = this.config.getInteger("step_timeS", 1);
            if (deta >= stepTime) {
                checkLed();
                this.testSignal.put(timePointKey, new Date(System.currentTimeMillis())); 
                addLog("Time", String.format(" delay: %s", stepTime - deta));
                try {
                    Thread.sleep(stepTime * 1000);
                } catch (InterruptedException ex) {
                }
            } else {
                try {
                    addLog("Time", String.format(" step time: %s < %s", deta, stepTime));
                    addLog("Time", String.format(" delay: %s", stepTime - deta));
                    Thread.sleep((stepTime - deta) * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private void checkLed() {
        String ip = this.config.getString("IP");
        Telnet telnet = this.functionBase.getTelnet(ip, 23);
        String comName = this.config.getString("dut_com");
        int braud = this.config.getInteger("braud", 115200);
        ComPort com = this.functionBase.getComport(comName, braud);
        if (telnet == null || com == null) {
            return;
        }
        try {
            String getLedValueCmd = this.config.getString("check_led_cmd");
            addLog("PC", "Led R");
            String ledRValue = getLedValue(telnet, com, "led_r", getLedValueCmd);
            addLog("VALUE", ledRValue);
            addLog("PC", "Led G");
            String ledGValue = getLedValue(telnet, com, "led_g", getLedValueCmd);
            addLog("VALUE", ledGValue);
            addLog("PC", "Led B");
            String ledBValue = getLedValue(telnet, com, "led_b", getLedValueCmd);
            addLog("VALUE", ledBValue);
            addLog("PC", "Led W");
            String ledWValue = getLedValue(telnet, com, "led_w", getLedValueCmd);
            addLog("VALUE", ledWValue);
            addLog("PC", "Led OFF");
            String ledOffValue = getLedValue(telnet, com, "led_off", getLedValueCmd);
            addLog("VALUE", ledOffValue);
            addLog("PC", "Save csv");
            this.csvLog.add(ledRValue, ledGValue, ledBValue, ledWValue, ledOffValue);
            addLog("PC", "Led W");
            String ledWValue1 = getLedValue(telnet, com, "led_w", getLedValueCmd);
            addLog("VALUE", ledWValue1);
        } finally {
            this.functionBase.disConnect(telnet);
            this.functionBase.disConnect(com);
        }
    }

    private String getLedValue(Telnet telnet, ComPort comPort, String key, String getLedValueCmd) {
        List<String> cmds = this.config.getListJsonArray(key);
        for (String cmd : cmds) {
            if (!this.functionBase.sendCommand(telnet, cmd)) {
                return null;
            }
        }
        if (!this.functionBase.sendCommand(comPort, getLedValueCmd)) {
            return null;
        }
        String line;
        TimeS timeS = new TimeS(3);
        while ((line = comPort.readLine()) != null && timeS.onTime()) {
            addLog("COM", line);
            if (line.contains("LED_R=")) {
                return line;
            }
        }
        return null;
    }

    private class CsvLog {

        private final FileService fileService;
        private final String fileName;
        private final TimeBase timeBase;

        public CsvLog(String fileName) {
            this.fileService = new FileService();
            this.fileName = fileName;
            this.timeBase = new TimeBase(TimeBase.UTC);
        }

        public void init() {
            this.fileService.writeFile(this.fileName, "Time,", true);
            this.fileService.writeFile(this.fileName, "LED R , LED R , LED R , LED R , LED R , LED R,", true);
            this.fileService.writeFile(this.fileName, "LED G , LED G , LED G , LED G , LED G , LED G,", true);
            this.fileService.writeFile(this.fileName, "LED B , LED B , LED B , LED B , LED B , LED B,", true);
            this.fileService.writeFile(this.fileName, "LED W , LED W , LED W , LED W , LED W , LED W,", true);
            this.fileService.writeFile(this.fileName, "LED OFF , LED OFF , LED OFF , LED OFF , LED OFF , LED OFF", true);
            this.fileService.writeFile(this.fileName, "\r\n", true);
        }

        public void add(String ledR, String ledG, String ledB, String ledW, String ledOff) {
            this.fileService.writeFile(this.fileName, String.format("%s ,", this.timeBase.getSimpleDateTime()), true);
            this.fileService.writeFile(this.fileName, String.format("%s ,", ledR), true);
            this.fileService.writeFile(this.fileName, String.format("%s ,", ledG), true);
            this.fileService.writeFile(this.fileName, String.format("%s ,", ledB), true);
            this.fileService.writeFile(this.fileName, String.format("%s ,", ledW), true);
            this.fileService.writeFile(this.fileName, String.format("%s", ledOff), true);
            this.fileService.writeFile(this.fileName, "\r\n", true);
        }

    }
}
