/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;

import Model.Interface.IPrepare;
import Control.InitPackages.InitProxy.IdPasswordProxy;
import Model.DataSource.Setting.KeyWord;
import Model.Interface.IInit;
import View.DrawBoardUI.FormDetail.AbsTabUI;
import View.DrawBoardUI.SubUI.AbsSubUi;
import View.DrawBoardUI.UIWarehouse.BigUIProxy;
import View.DrawBoardUI.UIWarehouse.SmallProxy;
import View.DrawBoardUI.UIWarehouse.TabItemProxy;
import View.DrawBoardUI.UIWarehouse.TabLogProxy;
import View.DrawBoardUI.UIWarehouse.TabViewProxy;

/**
 *
 * @author 21AK22
 */
public class Factory {

    private static volatile Factory instance;
    private final FactoryType<AbsSubUi> subUIFactory;
    private final FactoryType<AbsTabUI> tabUIFactory;
    private final FactoryType<IInit> initFunc;
    private final FactoryType<IPrepare> prepareFunc;

    private Factory() {
        this.subUIFactory = new FactoryType<>();
        addSubUI();
        this.tabUIFactory = new FactoryType<>();
        addTabUI();
        this.initFunc = new FactoryType<>();
        addInitFunc();
        this.prepareFunc = new FactoryType<>();
        addPrepareFunc();
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

    public IInit getInitFunc(String type) {
        return this.initFunc.takeIt(type);
    }
    
    public IPrepare getPrepareFunc(String type) {
        return this.prepareFunc.takeIt(type);
    }

    private void addSubUI() {
        this.subUIFactory.addType(new BigUIProxy(KeyWord.SubUI.BIG_UI));
        this.subUIFactory.addType(new SmallProxy(KeyWord.SubUI.SMAIL_UI));
    }

    private void addTabUI() {
        this.tabUIFactory.addType(new TabViewProxy(KeyWord.Detail.VIEW));
        this.tabUIFactory.addType(new TabItemProxy(KeyWord.Detail.ITEM));
        this.tabUIFactory.addType(new TabLogProxy(KeyWord.Detail.LOG));
    }

    private void addInitFunc() {
        this.initFunc.addType(new IdPasswordProxy(KeyWord.Init.PASSWORD));
    }

    private void addPrepareFunc() {
        this.prepareFunc.addType(new simpleCheckSfisProxy(KeyWord.Prepare.SIMPLE_SFIS));
    }
}
