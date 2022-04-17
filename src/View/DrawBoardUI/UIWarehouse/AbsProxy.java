/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.UIWarehouse;


/**
 *
 * @author Administrator
 * @param <T>
 */
public abstract class AbsProxy<T> {

    private final String type;
    private String name;

    public AbsProxy(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return this.type;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public abstract T takeIt();
}