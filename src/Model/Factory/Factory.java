/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.CheckSnFormSFIS.CheckSnFormSFISProxy;
import Control.Functions.FunctionsTest.FixtureAction.FixtureActionProxy;
import Control.Functions.FunctionsTest.GenerateNode.GenerateNodeProxy;
import Control.Functions.FunctionsTest.GetMacFromSfis.GetMacFormSfisProxy;
import Control.Functions.FunctionsTest.SendResultToSfis.SendResuttToSfisProxy;
import Control.Functions.InitPackages.InitProxy.IdPasswordProxy;
import View.subUI.FormDetail.AbsTabUI;
import View.subUI.SubUI.AbsSubUi;
import View.subUI.FormDetail.TabLog.TabLogProxy;
import Model.Interface.IFunction;
import View.subUI.FormDetail.TabItem.TabItemProxy;
import View.subUI.FormDetail.TabView.TabViewProxy;
import View.subUI.SubUI.BigUI.BigUIProxy;
import View.subUI.SubUI.SmallUI.SmallProxy;

/**
 *
 * @author 21AK22
 */
public class Factory {
    
    private static volatile Factory instance;
    private final FactoryType<AbsSubUi> subUIFactory;
    private final FactoryType<AbsTabUI> tabUIFactory;
    private final FactoryType<IFunction> initFunctions;
    private final FactoryType<AbsFunction> functions;
    
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
    
    public AbsSubUi getSubUI(String type, String index) {
        return this.subUIFactory.takeIt(type, index);
    }
    
    public AbsTabUI getTabUI(String type) {
        return this.tabUIFactory.takeIt(type);
    }
    
    public IFunction getInitFunc(String type) {
        return this.initFunctions.takeIt(type);
    }
    
    public AbsFunction getFunc(String type, String funcName) {
        return this.functions.takeIt(type, funcName);
    }
    
    private void addSubUI() {
        this.subUIFactory.addType(new BigUIProxy());
        this.subUIFactory.addType(new SmallProxy());
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
        this.functions.addType(new CheckSnFormSFISProxy());
        this.functions.addType(new SendResuttToSfisProxy());
        this.functions.addType(new GenerateNodeProxy());
        this.functions.addType(new GetMacFormSfisProxy());
        this.functions.addType(new FixtureActionProxy());
    }
}
