/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.FileType;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;

/**
 *
 * @author Administrator
 */
public interface ITypeRead {
    
    JSONObject readContain(BufferedReader reader) throws Exception;
}
