/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.FileType;

import Model.Interface.ITypeRead;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class FileErrorMode implements ITypeRead {

    private final String LINE_ERRCODE = "^[^\\\\].+,.+$";
    private final String REGEX_NAME = "[a-zA-Z]+[[^\\w,][\\w]+]*";
    private final String REGEX_VALUE = "[0-9]+[[^\\w,][0-9]+]*";
    private final JSONObject json;

    public FileErrorMode() {
        this.json = new JSONObject();
    }

    public void phanTich(String data) {
        if (data.matches(LINE_ERRCODE)) {
            String nameItem = findFirst(data, REGEX_NAME).trim();
            String value = findFirst(data, REGEX_VALUE).trim();
            json.put(nameItem, value);
        }
    }

    private String findFirst(String stringLine, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringLine);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    public JSONObject readContain(BufferedReader reader) {
        String readline;
        try {
            while ((readline = reader.readLine()) != null) {
                System.out.println(readline);
                phanTich(readline);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return this.json;
        }
    }
}
