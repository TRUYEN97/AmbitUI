/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InitPackages;

import Model.Interface.IInit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class idPassWord implements IInit {

    private final String passWord;
    private final String id;

    public idPassWord(String Id, String passWord) {
        this.passWord = passWord;
        this.id = Id;
    }

    @Override
    public boolean init() {
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField();
        Object[] ob = {jUserName, userName, jPassword, password};
        int result = JOptionPane.showConfirmDialog(null, ob, "Please input password!", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return userName.getText().equals(this.id) && password.getText().equals(this.passWord);
        }
        return false;
    }

}
