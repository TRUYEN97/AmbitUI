/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionKeyWord;
import Control.Functions.FunctionsTest.FixtureAction.FixtureActionProxy;
import Control.Functions.FunctionsTest.GenerateNode.GenerateNodeProxy;
import Control.Functions.FunctionsTest.GetMacFromSfis.GetMacFormSfisProxy;
import Control.Functions.InitPackages.InitProxy.IdPasswordProxy;
import View.subUI.FormDetail.AbsTabUI;
import View.subUI.SubUI.AbsSubUi;
import View.subUI.SubUiKeyWord;
import View.subUI.UiProxy.BigUIProxy;
import View.subUI.UiProxy.SmallProxy;
import View.subUI.UiProxy.TabItemProxy;
import View.subUI.UiProxy.TabLogProxy;
import View.subUI.UiProxy.TabViewProxy;
import Model.Interface.IFunction;

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
        this.subUIFactory.addType(new BigUIProxy(SubUiKeyWord.SubUI.BIG_UI));
        this.subUIFactory.addType(new SmallProxy(SubUiKeyWord.SubUI.SMAIL_UI));
    }
    
    private void addTabUI() {
        this.tabUIFactory.addType(new TabViewProxy(SubUiKeyWord.Detail.VIEW));
        this.tabUIFactory.addType(new TabItemProxy(SubUiKeyWord.Detail.ITEM));
        this.tabUIFactory.addType(new TabLogProxy(SubUiKeyWord.Detail.LOG));
    }
    
    private void addInitFunc() {
        this.initFunctions.addType(new IdPasswordProxy(SubUiKeyWord.Init.PASSWORD));
    }

    private void addFunc() {
        this.functions.addType(new GenerateNodeProxy(FunctionKeyWord.GENERATE_NODE));
        this.functions.addType(new GetMacFormSfisProxy(FunctionKeyWord.GET_MAC_FROM_SFIS));
        this.functions.addType(new FixtureActionProxy(FunctionKeyWord.FIXTURE_ACTION));
    }
}
