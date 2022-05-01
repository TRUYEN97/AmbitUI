/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Control.Core.Engine;
import Model.DataSource.Tool.LoadSource;

/**
 *
 * @author 21AK22
 */
public class Main {

    public static void main(String[] args) {
        new LoadSource().init();
        Engine core = new Engine();
        core.run();
    }
}
