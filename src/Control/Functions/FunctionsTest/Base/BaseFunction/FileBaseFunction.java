/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import FileTool.FileService;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FileBaseFunction extends AbsFunction {

    private final FileService fileService;

    public FileBaseFunction(String itemName) {
        super(itemName);
        fileService = new FileService();
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public boolean saveJson(JSONObject baseData) {
        String filePath = this.allConfig.getString("localFile");
        List<String> elementName = this.allConfig.getListJsonArray("ElementName");
        String nameFile = this.createNameFile(elementName, ".json");
        return saveFile(filePath, nameFile, formatJson(baseData.toJSONString()));
    }
    
    public boolean saveTxt(String baseData) {
        String filePath = this.allConfig.getString("localFile");
        List<String> elementName = this.allConfig.getListJsonArray("ElementName");
        String nameFile = this.createNameFile(elementName, ".txt");
        return saveFile(filePath, nameFile, baseData);
    }

    public boolean saveFile(String dirPath, String nameFile, String data) {
        String filePath = String.format("%s\\%s", dirPath, nameFile);
        if (fileService.saveFile(filePath, data)) {
            addLog("PC", String.format("Save data in: %s ok", filePath));
            return true;
        }
        addLog("PC", String.format("Save data in: %s failed!", filePath));
        return false;
    }

    public String formatJson(String str) {
        addLog("PC", "Format for json data!");
        str = str.replaceAll("\\},(?!\r\n)", "\r\n},\r\n");
        str = str.replaceAll("\\{(?!\r\n)", "\r\n{\r\n");
        str = str.replaceAll("\\}(?!,)", "\r\n}\r\n");
        str = str.replaceAll(",(?=\")", ",\r\n");
        return str.trim();
    }

    public String createNameFile(List<String> nameElement, String end) {
        StringBuilder pathName = new StringBuilder();
        for (String elem : nameElement) {
            if (!pathName.isEmpty()) {
                pathName.append("_");
            }
            String value = productData.getString(elem);
            if (value == null) {
                pathName.append(elem);
            } else {
                value = value.replace('\\', '-');
                value = value.replace('/', '-');
                pathName.append(value);
            }
        }
        return pathName.append(end).toString();
    }

}
