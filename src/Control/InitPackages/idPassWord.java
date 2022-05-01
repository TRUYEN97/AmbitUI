/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.InitPackages;

import Control.Functions.AbsFunction;
import java.awt.HeadlessException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class idPassWord extends AbsFunction {

    private final String passWord;
    private final String id;

    public idPassWord(String Id, String passWord) {
        this.passWord = passWord;
        this.id = Id;
    }

    @Override
    public void run() {
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField();
        Object[] ob = {jUserName, userName, jPassword, password};
        if (show(ob) == JOptionPane.OK_OPTION) {
            setPass(isAccept(userName, password));
        }else{
            setPass(false);
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

}
