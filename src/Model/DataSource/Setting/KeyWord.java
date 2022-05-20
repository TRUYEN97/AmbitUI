/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
final class KeyWord {

    public static final String STATION = "STATION";
    public static final String DHCP = "DHCP";
    public static final String COLUMN = "COLUMN";
    public static final String ROW = "ROW";
    public static final String DETAIL = "DETAIL";
    public static final String TYPE_UI = "TYPE_UI";
    public static final String LOAD_MODE = "LOAD_MODE";
    public static final String NAME = "NAME";
    public static final String TYPE_MODE = "TYPE_MODE";
    public static final String INIT_FUNC = "INIT";
    public static final String PREPARE = "PREPARE";
    public static final String DISCRETE_TEST = "DISCRETE_TEST";
    public static final String LOCAL_FUNCTION_LOG = "LOCAL_FUNCTION_LOG";
    public static final String LOCAL_LOG = "LOCAL_LOG";
    public static final String END = "END";
    public static final List<String> MODE_KEY = Arrays.asList(
            STATION, DHCP, COLUMN, ROW, DETAIL, DISCRETE_TEST, TYPE_UI, NAME, TYPE_MODE,
            INIT_FUNC, PREPARE, END, LOCAL_LOG);

    public class Init {

        public static final String PASSWORD = "Password";
    }

    public class Detail {

        public static final String VIEW = "View";
        public static final String LOG = "Log";
        public static final String ITEM = "Item";
    }

    public class SubUI {

        public static final String BIG_UI = "Big";
        public static final String SMAIL_UI = "Smail";
    }

    public class Prepare {

        public static final String CHECK_SN_FAIL = "CHECK_SN_FAIL";
        public static final String CHECK_FIXTURE = "CHECK_FIXTURE";
        public static final String SIMPLE_SFIS = "SIMPLE_SFIS";
        public static final String DEV_SFIS = "DEV_SFIS";
    }

    public class End {

        public static final String CUS_SERVER = "CUS_SERVER";
        public static final String FPT_SERVER = "FPT_SERVER";
        public static final String UP_API = "UP_API";
        public static final String DEV_SFIS = "DEV_SFIS";
    }
}
