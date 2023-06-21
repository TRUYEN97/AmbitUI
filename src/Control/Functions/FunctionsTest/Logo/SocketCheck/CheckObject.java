/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Logo.SocketCheck;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import Unicast.Client.Client;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CheckObject extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;
    private boolean pass;
    private String mess;
    private String action;
    private boolean stop;

    public CheckObject(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.fileBaseFunction = new FileBaseFunction(functionParameters, itemName);
        this.pass = false;
        this.mess = "";
        this.action = "";
        this.stop = false;
    }

    public CheckObject(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        String host = this.config.getString("host", "localhost");
        int port = this.config.getInteger("port", 60026);
        int time = this.config.getInteger("time", 10);
        List<String> objects = this.config.getJsonList("object");
        addLog(LOG_KEYS.CONFIG, "connect to socket server: %s - %s", host, port);
        addLog(LOG_KEYS.CONFIG, "timeout: %s", time);
        Client client = null;
        try {
            client = new Client(host, port, (String str) -> {
                try {
                    JSONObject data = JSONObject.parseObject(str);
                    addLog("Socket", data);
                    Boolean rs = data.getBoolean("status");
                    if (rs == null) {
                        return;
                    } else if (!rs) {
                        mess = data.getString("message");
                        addLog(LOG_KEYS.PC, "message: %s", mess);
                    }
                    String action = data.getString("action");
                    if (action == null) {
                        addLog(LOG_KEYS.PC, "ivalid action");
                    } else if (action.equalsIgnoreCase(this.action)) {
                        if (action.equalsIgnoreCase("test")) {
                            JSONArray array = data.getJSONArray("data");
                            pass = isPassData(array, objects);
                        } else if (action.equalsIgnoreCase("save")) {
                            pass = rs;
                        }
                    } else if (action.equalsIgnoreCase("replace")) {
                        stop = true;
                    }
                } catch (Exception e) {
                    addLog(LOG_KEYS.ERROR, e.getLocalizedMessage());
                }
            });
            TimeS timer = new TimeS(time);
            while (timer.onTime()) {
                if (client.connect() && client.isConnect()) {
                    break;
                }
                delay(500);
            }
            if (!client.isConnect()) {
                addLog(LOG_KEYS.PC, "connect to server failed!");
                return false;
            }
            Thread thread = new Thread(client);
            thread.setDaemon(true);
            thread.start();
            boolean checkModel = testting(timer, client);
            boolean saveImg = saveImg(client, checkModel);
            if (checkModel && saveImg) {
                return true;
            } else {
                this.processData.setMessage(mess);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (client != null) {
                client.disconnect();
                addLog(LOG_KEYS.PC, "disconnect to server!");
            }
        }

    }

    private boolean isPassData(JSONArray datas, List<String> objects) {
        if (datas == null || datas.isEmpty()) {
            addLog("pc", "datas == null or empty");
            return false;
        }
        boolean status = true;
        for (int i = 0; i < datas.size(); i++) {
            var data = datas.get(i);
            addLog("---------------------------------------");
            if (data instanceof JSONObject item) {
                Boolean rs = item.getBoolean("status");
                String name = item.getString("label");
                if (rs == null || !rs || name == null || !name.equalsIgnoreCase(objects.get(i))) {
                    status = false;
                }
                for (String key : item.keySet()) {
                    var value = item.get(key);
                    addLog(LOG_KEYS.PC, "%s - %s", key, value);
                }
            }
            addLog("---------------------------------------");
        }
        return status;
    }

    private boolean saveImg(Client client, boolean checkModel) {
        this.action = "save";
        JSONObject json = new JSONObject();
        String localFolder = createPath();
        String fileName = createName(checkModel);
        String imgdir = String.format("%s/%s", localFolder, fileName);
        addLog(LOG_KEYS.CONFIG, "image log: %s", imgdir);
        json.put("action", "save");
        json.put("dir", localFolder);
        json.put("name", fileName);
        pass = false;
        TimeS timer = new TimeS(5);
        while (timer.onTime() && client.isConnect() && !stop) {
            client.send(json.toString());
            addLog(LOG_KEYS.PC, "send to server: %s", json);
            delay(100);
            if (pass) {
                addLog(LOG_KEYS.PC, "save image ok");
                return true;
            }
        }
        addLog(LOG_KEYS.PC, "save image falsed");
        return false;
    }

    private String createPath() {
        List<String> localPrefix = this.config.getJsonList("LocalPrefix");
        return this.fileBaseFunction.createName(localPrefix);
    }

    private String createName(boolean checkModel) {
        String suffix = this.config.getString("suffix", "jpg");
        List<String> localName = this.config.getJsonList("LocalName");
        List<String> localNameFail = this.config.getJsonList("LocalNameFail", localName);
        return this.fileBaseFunction.createName(checkModel ? localName : localNameFail).concat(".").concat(suffix);
    }

    private boolean testting(TimeS timer, Client client) {
        JSONObject json = new JSONObject();
        json.put("action", "test");
        this.action = "test";
        String code = this.processData.getString(AllKeyWord.SFIS.SN, "debug");
        json.put("code", code);
        while (timer.onTime() && client.isConnect() && !stop) {
            client.send(json.toString());
            addLog(LOG_KEYS.PC, "send to server: %s", json);
            delay(100);
            if (pass) {
                addLog(LOG_KEYS.PC, "Detect model passed!");
                return true;
            }
        }
        addLog(LOG_KEYS.PC, "Detect model failed!");
        return false;
    }

    private void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
    }

}
