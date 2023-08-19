/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Control.Core.Engine;
import Model.ErrorLog;
import Model.DataSource.Tool.LoadSource;
import javax.swing.JOptionPane;

/**
 *
 * @author 21AK22
 */
public class Main {

    public static void main(String[] args) {
        try {
            String version = null;
            if(args.length > 0){
                version = args[0].trim();
            }
            if (!new LoadSource().init()) {
                String mess = "Main class! Init source files failed!!!";
                ErrorLog.addError(mess);
                JOptionPane.showMessageDialog(null, mess);
                System.exit(0);
            }
            Engine engine = new Engine(version);
            engine.run();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getMessage());
        }
    }
}
