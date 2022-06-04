/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CreateFaJson;

import Model.DataModeTest.InputData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class KeyWordFaAPI {

    public static final String CHILD_UNLINKED = "child_unlinked";
    public static final String DEBUG_COUNT = "debug_count";
    public static final String DEBUG_STATION = "debug_station";
    public static final String ERROR_CODE = "error_code";
    public static final String ERROR_DETAILS = "error_details";
    public static final String FAILED_STATION = "failed_station";
    public static final String FAILED_STATION_TYPE = "failed_station_type";
    public static final String FINISH_TIME = "finish_time";
    public static final String FURTHER_DETAILS = "further_details";
    public static final String MODE = "mode";
    public static final String POST_REPAIR_RESULT = "post_repair_result";
    public static final String REASON_DESC = "reason_desc";
    public static final String REPAIR_ACTION = "repair_action";
    public static final String REPAIR_LOCATION = "repair_location";
    public static final String SERIAL = "serial";
    public static final String START_TIME = "start_time";
    public static final String STATION_NAME = "station_name";
    public static final String STATION_TYPE = "station_type";
    public static final String STATUS = "status";
    public static final String TESTS = "tests";
    public static final String VERSION = "test_software_version";
    public static final String SCRAP = "scrap";
    public static final String LOG_TXT = "log";

    public static enum BASE_KEY {
        station_name(InputData.PCNAME),
        station_type(InputData.STATION),
        error_details(InputData.ERROR_DES),
        error_code(InputData.ERROR_CODE),
        serial(InputData.MLBSN),
        mode(InputData.MODE),
        status(InputData.STATUS),
        test_software_version(InputData.VERSION),
        start_time(InputData.START_TIME),
        finish_time(InputData.FINISH_TIME);
        private final String inputKey;

        BASE_KEY(String inputKey) {
            this.inputKey = inputKey;
        }

        public String getInputKey() {
            return this.inputKey;
        }

        @Override
        public String toString() {
            return name();
        }

    };

    public static final List<String> FUNC_KEY = Arrays.asList(new String[]{
        REPAIR_ACTION, FURTHER_DETAILS, FAILED_STATION_TYPE, FINISH_TIME,
        DEBUG_COUNT, REPAIR_LOCATION, REASON_DESC, POST_REPAIR_RESULT,
        START_TIME, DEBUG_STATION, FAILED_STATION, CHILD_UNLINKED
    });
}
