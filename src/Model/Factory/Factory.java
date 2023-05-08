/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.CheckDutInfo.CheckDutInfoProxy;
import Control.Functions.FunctionsTest.Base.CheckProduct.CheckProductProxy;
import Control.Functions.FunctionsTest.Base.CheckTestConditions.CheckTestConditionsProxy;
import Control.Functions.FunctionsTest.Base.CheckUpdate.CheckUpdateProxy;
import Control.Functions.FunctionsTest.Base.CountPassFailed.CountPassFailedProxy;
import Control.Functions.FunctionsTest.Base.CreatePath.CreatePathProxy;
import Control.Functions.FunctionsTest.Base.Delta.DeltaProxy;
import Control.Functions.FunctionsTest.Base.TxtLog.CreateLog.CreateTxtProxy;
import Control.Functions.FunctionsTest.Base.GetMacFromSfis.GetMacFormSfisProxy;
import Control.Functions.FunctionsTest.Base.DutPing.DutPingProxy;
import Control.Functions.FunctionsTest.Base.DutTelnet.DutTelnetProxy;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureActionProxy;
import Control.Functions.FunctionsTest.Base.GoldenFile.GoldenFileProxy;
import Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi.CreateJsonApiProxy;
import Control.Functions.FunctionsTest.Base.JsonApi.UpApi.UpApiProxy;
import Control.Functions.FunctionsTest.Base.MyDas.MydasProxy;
import Control.Functions.FunctionsTest.Base.ReadInputUntilStopItem.ReadComportUntilStopItemProxy;
import Control.Functions.FunctionsTest.Base.SFIS.SfisFunctionProxy;
import Control.Functions.FunctionsTest.Base.SendCommandInFileAndPing.SendCommandInFileAndPingProxy;
import Control.Functions.FunctionsTest.Base.TxtLog.ZipLog.ZipFileProxy;
import Control.Functions.FunctionsTest.Runin.PowerSwitch.PowerSwitchProxy;
import Control.Functions.FunctionsTest.Base.UpLogFTP.UpLogFTPProxy;
import Control.Functions.FunctionsTest.Base.VirFunction.VirFunctionProxy;
import Control.Functions.FunctionsTest.MBLT.OpenShort.OpenShortProxy;
import Control.Functions.FunctionsTest.MBLT.ThermalShutdown.ThermalShutdownProxy;
import Control.Functions.FunctionsTest.MBLT.UsbAside.UsbAsideProxy;
import Control.Functions.FunctionsTest.MBLT.VoltageTest.VoltageTestProxy;
import Control.Functions.FunctionsTest.MBLT.VoltageTest.checkVoltProxy;
import Control.Functions.FunctionsTest.ON_OFF.AsideOnOffPing.AsideOnOffPingProxy;
import Control.Functions.FunctionsTest.ON_OFF.OpenShortOnOff.OpenShortOnOffProxy;
import Control.Functions.FunctionsTest.Runin.CheckCommandTelnet.CheckCommandTelnetProxy;
import Control.Functions.FunctionsTest.Runin.MMC_BadBlock.MMC_BadBlockProxy;
import Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED.MMC_WR_SPEED_PROXY;
import Control.Functions.FunctionsTest.ON_OFF.PowerSwicthPing.PowerSwicthPingProxy;
import Control.Functions.FunctionsTest.Other.CheckLed_W_1H_SFT.CheckLed_W_1H_Proxy;
import Control.Functions.FunctionsTest.Other.Reboot_CheckLed1H_SFT.Reboot_CheckLed1H_SFT_Proxy;
import Control.Functions.FunctionsTest.RTT.BootSetup.BootSetupProxy;
import Control.Functions.FunctionsTest.Runin.CheckCommandCmd.CheckCommandCmdProxy;
import Control.Functions.FunctionsTest.Runin.TelnetReadUntilKey.TelnetReadUntilKeyProxy;
import Control.Functions.FunctionsTest.Runin.RebootSoft.RebootSoftProxy;
import Control.Functions.FunctionsTest.Runin.TempCPU.TempCPUJupiterProxy;
import Control.Functions.FunctionsTest.Runin.TempCPU.TempCPUProxy;
import Control.Functions.InitPackages.InitProxy.IdPasswordProxy;
import Model.DataTest.FunctionParameters;
import View.subUI.FormDetail.AbsTabUI;
import View.subUI.SubUI.AbsSubUi;
import View.subUI.FormDetail.TabLog.TabLogProxy;
import Model.Interface.IFunction;
import View.subUI.FormDetail.TabItem.TabItemProxy;
import View.subUI.FormDetail.TabView.TabViewProxy;
import View.subUI.SubUI.BigUI.BigUIProxy;
import View.subUI.SubUI.SmallUI.SmallUIProxy;

/**
 *
 * @author 21AK22
 */
public class Factory {
    
    private static volatile Factory instance;
    private final FactoryType<String, AbsSubUi> subUIFactory;
    private final FactoryType<String, AbsTabUI> tabUIFactory;
    private final FactoryType<String,IFunction> initFunctions;
    private final FactoryType<FunctionParameters, AbsFunction> functions;
    
    private Factory() {
        this.subUIFactory = new FactoryType<>();
        addSubUI();
        this.tabUIFactory = new FactoryType<>();
        addTabUI();
        this.initFunctions = new FactoryType<>();
        addInitFunc();
        this.functions = new FactoryType<>();
        addFunc();
    }
    
    public static Factory getInstance() {
        Factory ins = Factory.instance;
        if (ins == null) {
            synchronized (Factory.class) {
                ins = Factory.instance;
                if (ins == null) {
                    Factory.instance = ins = new Factory();
                }
            }
        }
        return ins;
    }
    
    public synchronized AbsSubUi getSubUI(String type, String index) {
        return this.subUIFactory.takeIt(type, index);
    }
    
    public synchronized AbsTabUI getTabUI(String type) {
        return this.tabUIFactory.takeIt(type);
    }
    
    public synchronized IFunction getInitFunc(String type) {
        return this.initFunctions.takeIt(type);
    }
    
    public synchronized AbsFunction getFunc( String functionName, FunctionParameters functionParameters) {
        return this.functions.takeIt(functionName, functionParameters);
    }
    
    private void addSubUI() {
        this.subUIFactory.addType(new BigUIProxy());
        this.subUIFactory.addType(new SmallUIProxy());
    }
    
    private void addTabUI() {
        this.tabUIFactory.addType(new TabViewProxy());
        this.tabUIFactory.addType(new TabItemProxy());
        this.tabUIFactory.addType(new TabLogProxy());
    }
    
    private void addInitFunc() {
        this.initFunctions.addType(new IdPasswordProxy());
    }

    private void addFunc() {
        this.functions.addType(new SfisFunctionProxy());
        this.functions.addType(new GetMacFormSfisProxy());
        this.functions.addType(new CreateTxtProxy());
        this.functions.addType(new UpApiProxy());
        this.functions.addType(new UpLogFTPProxy());
        this.functions.addType(new PowerSwitchProxy());
        this.functions.addType(new DutPingProxy());
        this.functions.addType(new DutTelnetProxy());
        this.functions.addType(new CheckCommandTelnetProxy());
        this.functions.addType(new CheckDutInfoProxy());
        this.functions.addType(new RebootSoftProxy());
        this.functions.addType(new MMC_BadBlockProxy());
        this.functions.addType(new FixtureActionProxy());
        this.functions.addType(new TelnetReadUntilKeyProxy());
        this.functions.addType(new CreateJsonApiProxy());
        this.functions.addType(new MMC_WR_SPEED_PROXY());
        this.functions.addType(new PowerSwicthPingProxy());
        this.functions.addType(new OpenShortProxy());
        this.functions.addType(new UsbAsideProxy());
        this.functions.addType(new OpenShortOnOffProxy());
        this.functions.addType(new AsideOnOffPingProxy());
        this.functions.addType(new VoltageTestProxy());
        this.functions.addType(new ThermalShutdownProxy());
        this.functions.addType(new checkVoltProxy());
        this.functions.addType(new CheckLed_W_1H_Proxy());
        this.functions.addType(new Reboot_CheckLed1H_SFT_Proxy());
        this.functions.addType(new GoldenFileProxy());
        this.functions.addType(new TempCPUProxy());
        this.functions.addType(new MydasProxy());
        this.functions.addType(new ZipFileProxy());
        this.functions.addType(new VirFunctionProxy());
        this.functions.addType(new DeltaProxy());
        this.functions.addType(new CheckProductProxy());
        this.functions.addType(new TempCPUJupiterProxy());
        this.functions.addType(new CheckUpdateProxy());
        this.functions.addType(new CreatePathProxy());
        this.functions.addType(new SendCommandInFileAndPingProxy());
        this.functions.addType(new CheckCommandCmdProxy());
        this.functions.addType(new ReadComportUntilStopItemProxy());
        this.functions.addType(new BootSetupProxy());
        this.functions.addType(new CountPassFailedProxy());
        this.functions.addType(new CheckTestConditionsProxy());
    }
}
