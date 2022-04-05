/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.LoadModelTime;

import View.LoadModelTime.subModeTime.DateVnMode;
import View.LoadModelTime.subModeTime.TimeCustomerMode;
import View.LoadModelTime.subModeTime.TimeVnMode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class LoadModeTime{

    private static volatile LoadModeTime _modeTime = new LoadModeTime();
    private JLabel lbText = null;
    private JPanel backgroundUi;
    private int timeDelay = 200;
    private boolean fisrtFlag;
    private final ArrayList<Color> colors;
    private final List<AbsModeTime> timeMode;
    private AbsModeTime currentMode;

    private LoadModeTime() {
        this.colors = new ArrayList<>();
        this.timeMode = new ArrayList<>();
        addColor();
        addTimeMode();
        this.fisrtFlag = true;
    }

    private void addTimeMode() {
        this.timeMode.add(new TimeVnMode(TimeVnMode.class.getSimpleName()));
        this.timeMode.add(new DateVnMode(DateVnMode.class.getSimpleName()));
        this.timeMode.add(new TimeCustomerMode(TimeCustomerMode.class.getSimpleName()));
    }

    private void addColor() {
        colors.add(new Color(0x50));
        colors.add(new Color(0x950));
        colors.add(new Color(0x1c60));
        colors.add(new Color(0x2b70));
        colors.add(new Color(0x4d60));
        colors.add(new Color(0x467f70));
        colors.add(new Color(0x68a870));
        colors.add(new Color(0x80bd6e));
        colors.add(new Color(0xafd570));
        colors.add(new Color(0xd3e767));
        colors.add(new Color(0xffe767));
        colors.add(new Color(0xffff5c));
        colors.add(new Color(0xffa23d));
    }

    public static LoadModeTime getInstance() {
        return _modeTime;
    }
    
     public AbsModeTime getCurrentMode() {
        if (this.currentMode == null) {
            return this.timeMode.get(0);
        }
        return this.currentMode;
    }

    public void next() {
        if (getNextIndex() >= this.timeMode.size()) {
            this.currentMode = this.timeMode.get(0);
        } else {
            this.currentMode = this.timeMode.get(getNextIndex());
        }
        currentMode.upDate();
    }

    public void run() {
        if (fisrtFlag) {
            runNewThread();
        }
    }

    private void runNewThread() {
        this.fisrtFlag = true;
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String data =  getCurrentMode().getValue();
                        updateBackground(data);
                        lbText.setText(data);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Message.WriteMessger.ShowAll("Mode Time type not true!\r\n%s", this.getClass().getName(), null, null);
                    }
                    try {
                        Thread.sleep(timeDelay);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            private void updateBackground(String data) throws NumberFormatException {
                if (data.contains(" : ")) {
                    int hour = Integer.valueOf(data.substring(0, 2).trim());
                    hour = hour < 12 ? hour % 12 : 23 - hour;
                    backgroundUi.setBackground(colors.get(hour));
                }
            }
        }.start();
    }
    private int getNextIndex() {
        if (this.currentMode == null) {
            return 0;
        }
        return this.timeMode.indexOf(this.currentMode) + 1;
    }

    public int setTimeDelay(int timeMs) {
        if (timeMs <= 0) {
            this.timeDelay = 1;
        } else {
            this.timeDelay = timeMs;
        }
        return timeMs;
    }

    public void setLabel(JLabel label) {
        this.lbText = label;
    }

    public void setBackground(JPanel panel) {
        this.backgroundUi = panel;
    }
}
