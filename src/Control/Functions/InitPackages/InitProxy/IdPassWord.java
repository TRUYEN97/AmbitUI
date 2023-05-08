/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.InitPackages.InitProxy;

import java.awt.HeadlessException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Model.Interface.IFunction;

/**
 *
 * @author Administrator
 */
public class IdPassWord implements IFunction {

    private final String passWord;
    private final String id;
    private final String mess;
    private boolean pass;

    public IdPassWord(String Id, String passWord, String mess) {
        this.passWord = passWord;
        this.id = Id;
        this.mess = mess;
    }

    @Override
    public void run() {
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField();
        Object[] ob;
        if (mess != null && !mess.isBlank()) {
            String message = String.format("<html>%s<html>",
                    this.mess.replaceAll("\r\n", "<br>"));
            ob = new Object[]{new JLabel(message)
                    ,jUserName, userName, jPassword, password};
        }else{
            ob = new Object[]{jUserName, userName, jPassword, password};
        }
        if (show(ob) == JOptionPane.OK_OPTION) {
            pass = (isAccept(userName, password));
        } else {
            pass = (false);
        }
    }

    private boolean isAccept(JTextField userName, JTextField password) {
        return userName.getText().equals(this.id) && password.getText().equals(this.passWord);
    }

    private int show(Object[] ob) throws HeadlessException {
        return JOptionPane.showConfirmDialog(null,
                ob, "Please input password!",
                JOptionPane.OK_CANCEL_OPTION);
    }

    @Override
    public boolean isPass() {
        return pass;
    }

    @Override
    public boolean isTesting() {
        return false;
    }

}
