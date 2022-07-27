/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.PcInformation;

/**
 *
 * @author 21AK22
 */
public class UiInformartion {

    private final DataWareHouse dataWareHouse;
    private final PcInformation pcInformation;

    public UiInformartion(String name, int COLUMN, int ROW, int ID) {
        this.dataWareHouse = new DataWareHouse();
        this.pcInformation = PcInformation.getInstance();
        this.dataWareHouse.put(AllKeyWord.PC_NAME, this.pcInformation.getPcName());
        this.dataWareHouse.put(AllKeyWord.STATION_NAME, this.pcInformation.getPcName());
        this.dataWareHouse.put(AllKeyWord.STATION_TYPE, getStationType(this.pcInformation.getPcName()));
        this.dataWareHouse.put(AllKeyWord.POSITION, name);
        this.dataWareHouse.put(AllKeyWord.COLUMN, COLUMN);
        this.dataWareHouse.put(AllKeyWord.ROW, ROW);
        this.dataWareHouse.put(AllKeyWord.UI_ID, ID);
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

    public String getString(String key) {
        return this.dataWareHouse.getString(key);
    }

    public Integer getInteger(String key) {
        return this.dataWareHouse.getInteger(key);
    }

    private String getStationType(String pcName) {
        if (!pcName.contains("-")) {
            return pcName;
        }
        return pcName.substring(0, pcName.lastIndexOf("-"));
    }

}
