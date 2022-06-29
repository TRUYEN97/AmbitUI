/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus;

import Model.DataTest.ProcessTest.ProcessData;
import Control.Core.Core;
import Control.Core.ModeTest;
import Control.Core.CellTest;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.DataTest.InputData;
import Model.DataTest.ProcessTestSignal;
import Model.DataTest.ProductData;
import Model.Interface.IUpdate;
import View.subUI.SubUI.AbsSubUi;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class UiStatus implements IUpdate {

    private final AbsSubUi subUi;
    private final String name;
    private final Core core;
    private final ProcessData processData;
    private final ProcessTestSignal signal;
    private final ProductData productData;
    private CellTest cellTest;
    private ModeTest modeTest;

    public UiStatus(AbsSubUi subUi, Core core) {
        this.core = core;
        this.subUi = subUi;
        this.name = subUi.getName();
        this.processData = new ProcessData(this);
        this.signal = new ProcessTestSignal();
        this.productData = new ProductData();
    }

    public ProcessData getProcessData() {
        return processData;
    }

    public AbsSubUi getSubUi() {
        return subUi;
    }

    public ModeTest getModeTest() {
        return this.modeTest;
    }

    public CellTest getCellTest() {
        return cellTest;
    }

    public boolean isTesting() {
        return this.cellTest != null && this.cellTest.isAlive();
    }

    @Override
    public boolean update() {
        if (!this.isTesting()) {
            this.modeTest = core.getCurrMode();
            if (this.subUi.update()) {
                return true;
            }
        }
        return false;
    }

    public boolean isName(String name) {
        if (isNull(name)) {
            return false;
        }
        return this.name.equals(name);
    }

    public boolean isUI(AbsSubUi ui) {
        if (isNull(ui)) {
            return false;
        }
        return this.subUi.equals(ui);
    }

    void setModeTest(ModeTest modeTest) {
        this.modeTest = modeTest;
    }

    public String getName() {
        return name;
    }

    public void startTest(InputData inputData, ModeTestSource testSource) {
        this.productData.setInputData(inputData);/
        this.cellTest = new CellTest(this, inputData, testSource);
        this.cellTest.start();
    }

    public ProcessTestSignal getSignal() {
        return signal;
    }

    public ProductData getProductData() {
        return productData;
    }
}
