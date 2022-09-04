/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Tool;

import Time.WaitTime.Class.TimeS;

/**
 *
 * @author 21AK22
 */
public class TestTimer extends TimeS implements IgetTime{
    
    @Override
    public double getRuntime(){
        return this.getTime();
    }
}
