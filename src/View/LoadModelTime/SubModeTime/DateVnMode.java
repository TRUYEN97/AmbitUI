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
public class DateVnMode extends AbsModeTime{

    public DateVnMode(String name) {
        super(name);
    }

    @Override
    public String getValue() {
       return timeBase.getDateTime(TimeBase.UTC7,TimeBase.DD__MM__YYYY).concat(" (UTC+7)");
    }
    
}
