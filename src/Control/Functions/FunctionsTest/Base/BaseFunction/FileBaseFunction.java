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

    public boolean saveJson(JSONObject data, String path) {
        return saveTxt(formatJson(data.toJSONString()), path);
    }

    public String createStringPath(String prefix, String fileName, String suffix) {
        List<String> elementDir = this.config.getJsonList(prefix);
        addLog(LOG_KEYS.CONFIG, "prefix: %s", elementDir);
        List<String> elementNames = this.config.getJsonList(fileName);
        addLog(LOG_KEYS.CONFIG, "File name: %s", elementNames);
        String suffer = this.config.getString(suffix);
        addLog(LOG_KEYS.CONFIG, "suffix: %s", suffer);
        String path = String.format("%s/%s.%s", createName(elementDir),
                createName(elementNames), suffer);
        addLog(LOG_KEYS.PC, "Path: %s", path);
        return path;
    }

    public boolean saveTxt(String data, String path) {
        return saveFile(path, data);
    }

    public boolean saveFile(String dirPath, String nameFile, String data) {
        String filePath = String.format("%s/%s", dirPath, nameFile);
        return saveFile(filePath, data);
    }

    public boolean saveFile(String filePath, String data) {
        if (fileService.saveFile(filePath, data)) {
            addLog("PC", String.format("Save data in: %s ok", filePath));
            return true;
        }
        addLog("PC", String.format("Save data in: %s failed!", filePath));
        return false;
    }

    public boolean saveZip(String zipPath, String filePath) {
        addLog("PC", String.format("%s -> %s", filePath, zipPath));
        if (zip.zipFile(zipPath, new File(filePath))) {
            addLog("PC", String.format("Save data in: %s ok", zipPath));
            return true;
        }
        addLog("PC", String.format("Save data in: %s failed!", zipPath));
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

    public String createName(List<String> elementName) {
        StringBuilder pathName = new StringBuilder();
        addLog(LOG_KEYS.CONFIG, "Name elements: %s", elementName);
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
        return pathName.toString();
    }
    public String createDir(List<String> elementDir) {
        StringBuilder pathName = new StringBuilder();
        addLog(LOG_KEYS.CONFIG, "Dir elements: %s", elementDir);
        for (String elem : elementDir) {
            if (!pathName.isEmpty()) {
                pathName.append("/");
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
        return pathName.toString();
    }

    public String createDefaultStringPath(boolean pass) {
        String fileNameKey = pass ? "LocalName" : "LocalNameFail";
        return createStringPath("LocalPrefix", fileNameKey, "LocalSuffix");
    }

}
