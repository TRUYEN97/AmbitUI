/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public final class Message {

    public static class WriteMessger {

        private static final String ERRO_TYPE = "Erro";
        private static final String MESSAGER_TYPE = "Messager";

        public synchronized static void Warning(String contain, Object pare1, Object pare2, Object pare3) {
            JOptionPane.showMessageDialog(null, String.format(contain, pare1, pare2, pare3), MESSAGER_TYPE, JOptionPane.PLAIN_MESSAGE);
        }

        public synchronized static void ConfixErro(String contain, Object pare1, Object pare2, Object pare3) {
            JOptionPane.showMessageDialog(null, String.format(contain, pare1, pare2, pare3), ERRO_TYPE, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        public synchronized static void Console(String contain, Object pare1, Object pare2, Object pare3) {
            System.out.println(String.format("\r\n".concat(contain).concat("\r\n////////////\r\n"), pare1, pare2, pare3));
        }

        public synchronized static void ShowAll(String contain, Object pare1, Object pare2, Object pare3) {
            Console(contain, pare1, pare2, pare3);
            Warning(contain, pare1, pare2, pare3);
        }

        public synchronized static void ShowErroAll(String contain, Object pare1, Object pare2, Object pare3) {
            Console(contain, pare1, pare2, pare3);
            ConfixErro(contain, pare1, pare2, pare3);
        }

        public synchronized static void nameAlreadyExistError(String name, String thisClassName) {
            ShowErroAll("The name: %s already exists!\r\n%s", name, thisClassName, null);
        }

        public synchronized static void nameNotAlreadyExistError(String name, String thisClassName) {
            ShowErroAll("The name: %s not already exists!\r\n%s", name, thisClassName, null);
        }
    }

    public static class ShowWarning {

        private static JTextArea _textMess;

        public static void addLbMess(JTextArea textMess) {
            _textMess = textMess;
        }

        public static void show(String text) {
            if (_textMess != null) {
                _textMess.setText(text);
            }
        }

        public static void sayHi() {
            show("Chúc mọi người làm việc vui vẻ!");
        }
    }
}
