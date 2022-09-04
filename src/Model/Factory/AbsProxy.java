/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Factory;


/**
 *
 * @author Administrator
 * @param <N>
 * @param <T>
 */
public abstract class AbsProxy<N,T> {

    private final String type;
    private N id;

    public AbsProxy() {
        this.type = this.getClass().getSimpleName();
    }

    public N getParameter() {
        return id;
    }

    public String getTypeName() {
        return this.type;
    }
    
    public void setID(N id) {
        this.id = id;
    }

    public abstract T takeIt();
}
