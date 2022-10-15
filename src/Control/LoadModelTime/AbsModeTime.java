/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.LoadModelTime;

import Time.TimeBase;


/**
 *
 * @author Administrator
 */
public abstract class AbsModeTime{

   protected TimeBase timeBase = null;
   private final String name;

    public AbsModeTime(String name) {
        this.name = name;
        timeBase = new TimeBase(TimeBase.UTC);
    }
   
    public abstract String getValue();
    
    public void upDate(){
        timeBase = new TimeBase(TimeBase.UTC);
    }
    
}
