/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

/**
 *
 * @author 21AK22
 */
public class UiInformartion {
    
    private final String name;
    private final int COLUMN;
    private final int ROW;
    private final int ID;

    public UiInformartion(String name,int COLUMN, int ROW, int ID) {
        this.name = name;
        this.COLUMN = COLUMN;
        this.ROW = ROW;
        this.ID = ID;
    }
    
    public String getName(){
        return name;
    }

    public int getCOLUMN() {
        return COLUMN;
    }

    public int getROW() {
        return ROW;
    }

    public int getID() {
        return ID;
    }

    
    
    
    
}
