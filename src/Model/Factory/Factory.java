/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Control.Functions.AbsFunction;
import Control.Functions.InitPackages.InitProxy.IdPasswordProxy;
import View.subUI.FormDetail.AbsTabUI;
import View.subUI.SubUI.AbsSubUi;
import View.subUI.SubUiKeyWord;
import View.subUI.UIWarehouse.BigUIProxy;
import View.subUI.UIWarehouse.SmallProxy;
import View.subUI.UIWarehouse.TabItemProxy;
import View.subUI.UIWarehouse.TabLogProxy;
import View.subUI.UIWarehouse.TabViewProxy;
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
        
    }
}
