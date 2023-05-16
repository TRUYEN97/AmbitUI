/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckUpdate;

import Communicate.Impl.Cmd.Cmd;
import Communicate.Impl.FtpClient.FtpClient;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import FileTool.FileService;
import FileTool.MD5;
import Model.AllKeyWord;
import Model.DataSource.ProgramInformation;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeMs;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class CheckUpdate extends AbsFunction {

    private final FunctionBase baseFunc;
    private final FileService fileService;

    public CheckUpdate(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.baseFunc = new FunctionBase(functionParameters, itemName);
        this.fileService = new FileService();
    }

    public CheckUpdate(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        try {
            String user = this.config.getString("User", "config");
            String passWord = this.config.getString("Password", "eero");
            String host = this.config.getString("Host");
            int port = this.config.getInteger("Port", 21);
            FtpClient ftp = this.baseFunc.initFtp(user, passWord, host, port);
            String uutModel = this.config.getString("uutModel", this.processData.getString(AllKeyWord.CONFIG.DUT_MODEL));
            String pnname = this.processData.getString("pnname", this.config.getString("pnname"));
            String station = this.processData.getString(AllKeyWord.STATION_TYPE).toUpperCase();
            addLog("PC", "type name: %s, product: %s, station: %s", pnname, uutModel, station);
            String ftpVersionPath = String.format("CONFIG/%s/%s/AmbitVersion.ini", uutModel, pnname);
            String ftpVersionMD5Path = String.format("CONFIG\\%s\\%s\\AmbitMD5.ini", uutModel, pnname);
            String version = getVersionProperti(ftp, ftpVersionPath, station);
            String serverMD5 = getVersionProperti(ftp, ftpVersionMD5Path, station);
            String localMD5 = getLocalMD5();
            int count = this.uiStatus.getuIManager().countTesting();
            addLog("PC", "Version: %s", version);
            addLog("PC", "Version MD5: %s", serverMD5);
            addLog("PC", "Local version MD5: %s", localMD5);
            addLog("PC", "Testing: %s", count);
            if (!new File(VERSION_PATH).exists()
                    && !writeLocalversion(version)) {
                addLog(LOG_KEYS.PC, "Update version failed!");
                return false;
            }
            String localversion = this.fileService.readFile(new File(VERSION_PATH));
            addLog("PC", "Local version: %s", localversion);
            if ((!checkMD5(localMD5, serverMD5) || !checkVersion(version, localversion)) && count == 1) {
                reDownload(uutModel, pnname, station);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean checkVersion(String version, String localversion) {
        return version != null && localversion != null && version.trim().equals(localversion.trim());
    }

    private static boolean checkMD5(String localMD5, String serverMD5) {
        return localMD5 != null && serverMD5 != null && localMD5.trim().equals(serverMD5.trim());
    }

    private boolean writeLocalversion(String version) {
        if (this.fileService.writeFile(VERSION_PATH, version, false)) {
           ProgramInformation.getInstance().setVersion(version);
           return true; 
        }
        return  false;
    }
    private static final String VERSION_PATH = "init/version.txt";

    private String getLocalMD5() throws IOException, NoSuchAlgorithmException {
        File localFile = new File("AmbitUI.jar");
        if (!localFile.exists()) {
            return null;
        }
        return new MD5().getMD5(localFile);
    }

    private String getVersionProperti(FtpClient ftp, String ftpPath, String station) throws IOException {
        String version_ini = ftp.readStringFile(ftpPath);
        if (version_ini == null) {
            return null;
        }
        Properties ini_version = new Properties();
        ini_version.load(new StringReader(version_ini));
        return ini_version.getProperty(station);
    }

    private void reDownload(String uutModel, String pnname, String station) {
        String command = String.format("start AutoDL.bat %s %s %s", uutModel, pnname, station);
        Cmd cmd = new Cmd();
        cmd.sendCommand(command);
        System.out.println(cmd.readAll(new TimeMs(500)));
        System.exit(0);
    }

}
