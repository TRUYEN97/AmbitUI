/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class ClientConfig {
    private final DataWareHouse wareHouse;
    private final String name;

    public ClientConfig(String name, JSONObject jsono){
        this.wareHouse = new DataWareHouse(jsono);
        this.name = name;
    }
    
    public String getHost(){
        return this.wareHouse.getString("host");
    }
    
    public int getPort(){
        return this.wareHouse.getInteger("port", 0);
    }

    public String getName() {
        return name;
    }
}
