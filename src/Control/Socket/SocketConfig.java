/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Control.Functions.FunctionsTest.Base.CheckProduct.ConditionModel;
import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public List<ClientConfig> getClientConfigs(){
        List<ClientConfig> rs = new ArrayList<>();
        JSONObject clients = this.wareHouse.getJson("clients");
        for (String key : clients.keySet()) {
            rs.add(new ClientConfig(key, clients.getJSONObject(key)));
        }
        return rs;
    }
    

}
