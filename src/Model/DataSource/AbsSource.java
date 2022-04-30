/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.Interface.IInit;

/**
 *
 * @author 21AK22
 */
public abstract class AbsSource implements IInit {

    protected ReadFileSource readFile;

    public AbsSource setPath(String path) {
        if (readFile.setPath(path)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean init() {
        return this.readFile.init();
    }

}
