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
public class TimeCustomerMode extends AbsModeTime{

    public TimeCustomerMode(String name) {
        super(name);
    }

    @Override
    public String getValue() {
       return timeBase.getDateTime(TimeBase.UTC,TimeBase.HH_MM_SS).concat(" (UTC)");
    }
    
}
