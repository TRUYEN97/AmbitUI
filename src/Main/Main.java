/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Control.Core.Core;
import Model.DataSource.LoadSource;

/**
 *
 * @author 21AK22
 */
public class Main {

    public static void main(String[] args) {
        new LoadSource().init();
        Core core = new Core();
        core.run();
    }
}
