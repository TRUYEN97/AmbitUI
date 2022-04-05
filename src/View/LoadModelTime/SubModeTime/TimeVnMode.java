/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.LoadModelTime.subModeTime;

import Control.Time.TimeBase;
import View.LoadModelTime.AbsModeTime;

/**
 *
 * @author Administrator
 */
public class TimeVnMode extends AbsModeTime{

    public TimeVnMode(String name) {
        super(name);
    }

    
    @Override
    public String getValue() {
        return timeBase.getDateTime(TimeBase.UTC7,TimeBase.HH_MM_SS).concat(" (UTC+7)");
    }
}
