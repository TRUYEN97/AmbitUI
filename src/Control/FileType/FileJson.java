/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.FileType;

import Model.Interface.ITypeRead;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;

/**
 *
 * @author Administrator
 */
public class FileJson implements ITypeRead {

    @Override
    public JSONObject readContain(BufferedReader reader) throws Exception {
        StringBuilder buffer = new StringBuilder();
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                buffer.append(readLine).append("\r\n");
                System.out.println(readLine);
            }
            return JSONObject.parseObject(buffer.toString());
    }
}
