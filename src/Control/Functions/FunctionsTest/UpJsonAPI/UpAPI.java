/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.UpJsonAPI;

import Control.Functions.AbsFunction;
import commandprompt.Communicate.Cmd.Cmd;

/**
 *
 * @author Administrator
 */
public class UpAPI extends AbsFunction {

    private String filePath;

    public UpAPI(String itemName) {
        super(itemName);
    }

    @Override
    public boolean test() {
        addLog("Get filePath from signal!");
        String keyword = this.allConfig.getString("keyWord");
        addLog(" -With keyWord: " + keyword);
        if (getFilePath(keyword)) {
            return upAPI();
        }
        return false;
    }

    private boolean getFilePath(String keyword) {
        var path = uiData.getSignal(keyword);
        if (path == null) {
            addLog(String.format("keyWord: %s not avaiable!", keyword));
            return false;
        }
        addLog(String.format("File path is: ", path));
        this.filePath = path.toString();
        return true;
    }

    private boolean upAPI() {
        final String keyWord = "CmdCommand";
        addLog("Get cmd command. KeyWord:" + keyWord);
        String command = getCmdComand(keyWord);
        if (command == null) {
            return false;
        }
        addLog("Up api.....");
        Cmd cmd = new Cmd();
        if (!cmd.sendCommand(command)) {
            addLog(String.format("Run: \"%s\" failed!", command));
            return false;
        }
        String response = cmd.readAll();
        if (response == null || response.isBlank()) {
            addLog("API has not reply!");
            return false;
        }
        addLog(response);
        return isUpSuccess(response);
    }

    private String getCmdComand(final String keyWord) {
        if (this.allConfig.getString(keyWord) == null) {
            addLog(String.format("function config missing \"%s\" key", keyWord));
            return null;
        }
        String command = String.format("%s%s", this.allConfig.getString(keyWord), this.filePath);
        addLog("Cmd comand : " + command);
        return command;
    }

    private boolean isUpSuccess(String response) {
        return response != null && response.trim().endsWith("200");
    }

}
