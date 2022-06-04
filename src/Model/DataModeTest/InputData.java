/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest;

import Model.DataSource.PcInformation;
import com.alibaba.fastjson.JSONObject;
import java.util.Set;

/**
 *
 * @author 21AK22
 */
public class InputData {

    public static final String FAIL_PC = "failedpc";
    public static final String PC_NAME = "pcname";
    public static final String MODE = "mode";
    public static final String DEBUG_PC = "debugpc";
    public static final String ERROR_CODE = "errorcode";
    public static final String ERROR_DES = "errordes";
    public static final String COUNTTEST = "counttest";
    public static final String SN = "sn";
    public static final String MLBSN = "mlbsn";
    public static final String PCNAME = "pcname";
    public static final String STATUS = "status";
    public static final String INDEX = "index";
    public static final String RESULT = "result";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String STATION = "station";
    public static final String VERSION = "version";
    public static final String START_TIME = "start_time";
    public static final String FINISH_TIME = "finish_time";
    private final JSONObject data;

    public InputData() {
        this.data = new JSONObject();
//        this.data.put(PCNAME, PcInformation.getInstance().getPcName());
        this.data.put(PCNAME, "RTT-5014");
    }

    public void setInput(String input) {
        this.data.put(SN, input);
    }

    public String getInput() {
        return this.data.getString(SN);
    }

    public String getIndex() {
        return this.data.getString(INDEX);
    }

    public void setIndex(String index) {
        this.data.put(INDEX, index);
    }
    
    public String getValue(String key)
    {
        return this.data.getString(key);
    }

    public Set<String> getkeySet() {
        return this.data.keySet();
    }

}
