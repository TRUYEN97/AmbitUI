/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import FileTool.FileService;
import FileTool.Zip;
import Model.DataTest.FunctionParameters;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FileBaseFunction extends AbsFunction {

    private final FileService fileService;
    private final Zip zip;

    public FileBaseFunction(FunctionParameters parameters, String item) {
        super(parameters, item);
        fileService = new FileService();
        zip = new Zip();
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public boolean saveJson(JSONObject data) {
        String filePath = this.config.getString("localFile");
        List<String> elementName = this.config.getListJsonArray("ElementName");
        String nameFile = this.createNameFile(elementName,".json");
        return saveFile(filePath, nameFile, formatJson(data.toJSONString()));
    }

    public boolean saveTxt(String data) {
        String filePath = this.config.getString("localFile");
        List<String> elementName = this.config.getListJsonArray("ElementName");
        String nameFile = this.createNameFile( elementName, ".txt");
        return saveFile(filePath, nameFile, data);
    }
   
    public boolean saveFile(String dirPath, String nameFile, String data) {
        String filePath = String.format("%s%s%s", dirPath,File.pathSeparator, nameFile);
        if (fileService.saveFile(filePath, data)) {
            addLog("PC", String.format("Save data in: %s ok", filePath));
            return true;
        }
        addLog("PC", String.format("Save data in: %s failed!", filePath));
        return false;
    }
    
    public boolean saveZip(String dirPath, String zipName, String fileName) {
        String zipPath = String.format("%s\\%s", dirPath, zipName);
        String filePath = String.format("%s\\%s", dirPath, fileName);
        addLog("PC", String.format("%s -> %s", filePath, zipPath));
        if (zip.zipFile(zipPath, new File(filePath))) {
            addLog("PC", String.format("Save data in: %s ok", zipPath));
            return true;
        }
        addLog("PC", String.format("Save data in: %s failed!", zipPath));
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

    public String createNameFile(List<String> elementName, String end) {
        StringBuilder pathName = new StringBuilder();
        addLog("PC", "ElementName: " + elementName);
        for (String elem : elementName) {
            if (!pathName.isEmpty()) {
                pathName.append("_");
            }
            String value = processData.getString(elem);
            if (value == null) {
                pathName.append(elem);
            } else {
                value = value.replace('\\', '-');
                value = value.replace('/', '-');
                value = value.replace(' ', '-');
                value = value.replace(':', '-');
                pathName.append(value);
            }
        }
        return pathName.append(end).toString();
    }
    
    public String createDirPath(List<String> elementName) {
        StringBuilder pathName = new StringBuilder();
        addLog("PC", "ElementName: " + elementName);
        for (String elem : elementName) {
            if (!pathName.isEmpty()) {
                pathName.append("/");
            }
            String value = processData.getString(elem);
            if (value == null) {
                pathName.append(elem);
            }else{ 
                value = value.replace(' ', '-');
                value = value.replace(':', '-');
                pathName.append(value);
            }
        }
        return pathName.toString();
    }

}
