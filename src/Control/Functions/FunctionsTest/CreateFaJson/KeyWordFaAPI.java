/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CreateFaJson;

import Model.AllKeyWord;
import Model.DataTest.InputData;
import View.subUI.FormDetail.TabFaApi.TabFaApi;

/**
 *
 * @author Administrator
 */
public class KeyWordFaAPI {

    public static enum BASE_KEY {
        station_name(AllKeyWord.PCNAME),
        station_type(AllKeyWord.STATION),
        error_details(AllKeyWord.ERROR_DES),
        error_code(AllKeyWord.ERROR_CODE),
        serial(AllKeyWord.MLBSN),
        status(AllKeyWord.STATUS),
        test_software_version(AllKeyWord.VERSION),
        start_time(AllKeyWord.START_TIME),
        finish_time(AllKeyWord.FINISH_TIME);
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
        finish_time(AllKeyWord.FINISH_TIME),
        repair_location(TabFaApi.LOCATION),
        reason_desc(TabFaApi.REASON_DES),
        start_time(AllKeyWord.START_TIME),
        debug_station(AllKeyWord.DEBUG_PC),
        failed_station(AllKeyWord.FAIL_PC),
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
