package over.server;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FontEditor {

    private SimpleAttributeSet attributeSet;

    public FontEditor() {
        attributeSet = new SimpleAttributeSet();
    }

    public void setBold(JTextPane editor, String text){

        StyleConstants.setBold(attributeSet, true);

        try {
            int position = editor.getStyledDocument().getLength();

            editor.getStyledDocument().insertString(position, text, attributeSet);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(JTextPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSimple(JTextPane editor, String text){

        StyleConstants.setBold(attributeSet, false);

        try {
            int position = editor.getStyledDocument().getLength();

            editor.getStyledDocument().insertString(position, text, attributeSet);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(JTextPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}