/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class SocketConfig {

    private final DataWareHouse wareHouse;

    public SocketConfig() {
        this.wareHouse = new DataWareHouse();
    }

    public void setData(Map data) {
        if (data == null) {
            return;
        }
        this.wareHouse.putAll(data);
    }
    
    public boolean isOnSocket(){
        return this.wareHouse.getBoolean(AllKeyWord.CONFIG.FLAG);
    }
    
    public String getHost(){
        return this.wareHouse.getString("host");
    }
    
    public int getPort(){
        return this.wareHouse.getInteger("port", 0);
    }

    boolean isConnect() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
