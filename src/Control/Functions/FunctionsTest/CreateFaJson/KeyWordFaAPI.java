/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CreateFaJson;

import Model.DataModeTest.InputData;
import View.subUI.FormDetail.TabFaApi.TabFaApi;

/**
 *
 * @author Administrator
 */
public class KeyWordFaAPI {

    public static enum BASE_KEY {
        station_name(InputData.PCNAME),
        station_type(InputData.STATION),
        error_details(InputData.ERROR_DES),
        error_code(InputData.ERROR_CODE),
        serial(InputData.MLBSN),
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

    public static enum FUNC_KEY {
        further_details(TabFaApi.REPAIR_DETAIL),
        finish_time(InputData.FINISH_TIME),
        repair_location(TabFaApi.LOCATION),
        reason_desc(TabFaApi.REASON_DES),
        start_time(InputData.START_TIME),
        debug_station(InputData.DEBUG_PC),
        failed_station(InputData.FAIL_PC),
        repair_action(TabFaApi.ACTION);
        private final String inputKey;
        
        FUNC_KEY(String inputKey) {
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
}
