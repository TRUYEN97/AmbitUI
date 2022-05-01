/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.DataSource.Tool.ReadFileSource;
import Control.FileType.FileJson;
import Model.Interface.IInit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21AK22
 * @param <AbsElementInfo>
 */
public abstract class AbsJsonSource<AbsElementInfo> implements IInit {

    protected final ReadFileSource readFile;
    protected final List<AbsElementInfo> elements;

    protected AbsJsonSource() {
        elements = new ArrayList<>();
        this.readFile = new ReadFileSource(new FileJson());
    }

    public AbsJsonSource setPath(String path) {
        if (readFile.setPath(path)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean init() {
        if (this.readFile.init()) {
            return getData();
        }
        return false;
    }
    
    public List<AbsElementInfo> getElments() {
        return elements;
    }

    public int getCountMode() {
        return this.elements.size();
    }

    protected abstract boolean getData();

}
