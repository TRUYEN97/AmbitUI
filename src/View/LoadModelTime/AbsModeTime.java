/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.LoadModelTime;

import Control.Time.TimeBase;

/**
 *
 * @author Administrator
 */
public abstract class AbsModeTime{

   protected TimeBase timeBase = null;
   private String name;

    public AbsModeTime(String name) {
        this.name = name;
        timeBase = new TimeBase();
    }
   
    public abstract String getValue();
    
    public void upDate(){
        timeBase = new TimeBase();
    }
    
}
