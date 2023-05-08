/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckTestConditions;

import Control.Functions.AbsFunction;
import Control.Functions.InitPackages.InitProxy.IdPassWord;
import Model.DataTest.FunctionParameters;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class CheckTestConditions extends AbsFunction {

    public CheckTestConditions(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    public CheckTestConditions(FunctionParameters parameter) {
        this(parameter, null);
    }

    @Override
    protected boolean test() {
        int holpTimes = this.config.getInteger("hold", 3);
        addLog("CONFIG", "hold when %s times failed!", holpTimes);
        int failedConsecutive = this.uIInfo.getTestFailedConsecutive();
        addLog("PC", "times failed: %s", failedConsecutive);
        if (failedConsecutive >= holpTimes) {
            String mess = String.format("""
                          This position has exceeded the allowable number of failed tests.\r\n
                          Vị trí này đã test fail liên tục %s lần. Cần TE xác nhận để có thể chạy tiếp!
                          """, failedConsecutive);
            String id = this.config.getString("id", "TE");
            String pw = this.config.getString("pw", "Foxconn168!!");
            IdPassWord confirm = new IdPassWord(id, pw, mess);
            confirm.run();
            if (confirm.isPass()) {
                addLog("PC", "Has been confirmed by %s!", id);
                return true;
            } else {
                addLog(LOG_KEYS.PC, mess);
                this.processData.setMessage(mess);
                return false;
            }
        }
        return true;
    }

}
