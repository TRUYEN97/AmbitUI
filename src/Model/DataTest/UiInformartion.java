/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ProgramInformation;

/**
 *
 * @author 21AK22
 */
public class UiInformartion {

    private final DataWareHouse dataWareHouse;
    private final ProgramInformation pcInformation;

    public UiInformartion(String name, int COLUMN, int ROW, int ID) {
        this.dataWareHouse = new DataWareHouse();
        this.pcInformation = ProgramInformation.getInstance();
        this.dataWareHouse.putAll(this.pcInformation.toJson());
        this.dataWareHouse.put(AllKeyWord.POSITION, name);
        this.dataWareHouse.put(AllKeyWord.COLUMN, COLUMN);
        this.dataWareHouse.put(AllKeyWord.ROW, ROW);
        this.dataWareHouse.put(AllKeyWord.UI_ID, ID);
    }
    
    public void setStationType(String station){
        this.dataWareHouse.put(AllKeyWord.STATION_TYPE, station);
    }
    
    public String getName() {
        return this.getString(AllKeyWord.POSITION);
    }

    public int getCOLUMN() {
        return this.getInteger(AllKeyWord.COLUMN);
    }

    public int getROW() {
        return this.getInteger(AllKeyWord.ROW);
    }

    public int getID() {
        return this.getInteger(AllKeyWord.UI_ID);
    }

    public int getTestCount() {
        return getInteger(AllKeyWord.TEST_COUNT, 0);
    }
    
    public int getTestFailed() {
        return getInteger(AllKeyWord.TEST_F_COUNT, 0);
    }
    
    public int getTestFailedConsecutive() {
        return getInteger(AllKeyWord.TEST_FC_COUNT, 0);
    }
    
    public void resetFailedConsecutive() {
        this.dataWareHouse.put(AllKeyWord.TEST_FC_COUNT,0);
    }
    
    public int addTestCount() {
        return addOne(AllKeyWord.TEST_COUNT);
    }
    
    public int addTestFailCount() {
        return addOne(AllKeyWord.TEST_F_COUNT);
    }
    
    public int addTestFailConsecutiveCount() {
        return addOne(AllKeyWord.TEST_FC_COUNT);
    }

    private int addOne(String key) {
        int count = getInteger(key, 0)+1;
        this.dataWareHouse.put(key,count);
        return count;
    }

    public void setString(String key, String val) {
        this.dataWareHouse.put(key, val);
    }
    
    public String getString(String key) {
        return this.dataWareHouse.getString(key);
    }

    public Integer getInteger(String key) {
        return this.dataWareHouse.getInteger(key);
    }

    public Integer getInteger(String key, int defaultValue) {
        return this.dataWareHouse.getInteger(key, defaultValue);
    }

}
