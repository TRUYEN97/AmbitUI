/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.DataSource.Tool.ReadFileSource;
import Control.FileType.FileJson;
import Model.ErrorLog;
import Model.Interface.IInit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 21AK22
 * @param <T>
 * @param <AbsElementInfo>
 */
public abstract class AbsJsonSource<T, AbsElementInfo> implements IInit {

    protected final ReadFileSource readFile;
    protected final List<AbsElementInfo> elements;
    protected final HashMap<T, AbsElementInfo> mapElemnts;

    protected AbsJsonSource() {
        this.elements = new ArrayList<>();
        this.mapElemnts = new HashMap<>();
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
            resetData();
            return getData();
        }
        return false;
    }

    public HashMap<T, AbsElementInfo> getMapElemnts() {
        return mapElemnts;
    }

    public List<AbsElementInfo> getElments() {
        return elements;
    }

    public AbsElementInfo getElement(T name) {
        return this.mapElemnts.get(name);
    }

    public void put(T name, AbsElementInfo element) {
        this.elements.add(element);
        this.mapElemnts.put(name, element);
    }

    protected boolean resetData() {
        try {
            this.elements.clear();
            this.mapElemnts.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    public int getCountElement() {
        return this.elements.size();
    }

    protected abstract boolean getData();

}
