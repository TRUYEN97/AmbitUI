/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Limit;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
class KeyWord {

    public static final String MODEL = "model";
    public static final String STATION = "station_type";
    public static final String TIME_STAMP = "timestamp";
    public static final String LIMITS_VALIDATION = "limits_validation";
    public static final String LIMITS = "limits";
    public static final String ID = "id";
    public static final String LAST_UPDATE = "last_updated_at";
    public static final String TEST_NAME = "test_name";
    public static final String LIMIT_TYPE = "limit_type";
    public static final String REQUIRED = "required";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String UNITS = "units";
    public static final String ERROR_CODE = "error_code";
    public static final String LOCKED = "locked";
    public static final List<String> KEYS = Arrays.asList(MODEL, TIME_STAMP,
            LIMITS_VALIDATION, ID, TEST_NAME, LIMIT_TYPE, REQUIRED,
            LOWER_LIMIT, UPPER_LIMIT, UNITS, ERROR_CODE, LOCKED);
}
