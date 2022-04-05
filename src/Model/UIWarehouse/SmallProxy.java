/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.UIWarehouse;

import View.DrawBoardUI.SubUI.AbsSubUi;
import View.DrawBoardUI.SubUI.SmallUI;

/**
 *
 * @author Administrator
 */
public class SmallProxy extends AbsProxyUI{

    public SmallProxy(String type) {
        super(type);
    }

    @Override
    public AbsSubUi getUI(String indexName) {
       return new SmallUI(indexName);
    }
    
}
