/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.FileType;

import Model.Interface.ITypeRead;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class FileConfigMode implements ITypeRead {

    private static final String REGEX_LINE_PRODUCT = "^[^\\\\].+=.+;.*$";
    private JSONObject json;

    public FileConfigMode() {
        json = new JSONObject();
    }

    public void phanTich(String data) {
        if (data.matches(REGEX_LINE_PRODUCT)) {
            String nameItem = data.substring(0, data.indexOf("=")).trim();
            String value = data.substring(data.indexOf("=") + 1, data.indexOf(";")).trim();
            this.json.put(nameItem, value);
        }
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
        }finally{
            return this.json;
        }
    }
}
