/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckUpdate;

import Communicate.Impl.Cmd.Cmd;
import Communicate.Impl.FtpClient.FtpClient;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import FileTool.MD5;
import Model.AllKeyWord;
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

    public CheckUpdate(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.baseFunc = new FunctionBase(functionParameters, itemName);
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
            String uutModel = this.processData.getString("uutModel");
            String pnname = this.processData.getString("pnname", this.config.getString("pnname"));
            String station = this.processData.getString(AllKeyWord.STATION_TYPE).toUpperCase();
            addLog("PC", "Pn name: %s, uut model: %s, station: %s", pnname, uutModel, station);
            // VersionPath = String.format("CONFIG/%s/%s/AmbitVersion.ini", uutModel, pnname);
            String ftpVersionMD5Path = String.format("CONFIG\\%s\\%s\\AmbitMD5.ini", uutModel, pnname);
            // String version = getVersionProperti(ftp, ftpVersionPath, station);
            String serverMD5 = getVersionProperti(ftp, ftpVersionMD5Path, station);
            String localMD5 = getLocalMD5();
            int count = this.uiStatus.getuIManager().countTesting();
            addLog("PC", "Version MD5: %s", serverMD5);
            addLog("PC", "Local version MD5: %s", localMD5);
            addLog("PC", "Testing: %s", count);
            if (localMD5 == null
                    || (serverMD5 != null && !serverMD5.equals(localMD5)
                    && count == 1)) {
                reDownload(uutModel, pnname, station);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

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
